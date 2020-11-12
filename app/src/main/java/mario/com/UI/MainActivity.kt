package mario.com.UI

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import mario.com.API.*
import mario.com.Extension.action
import mario.com.Extension.displaySnack
import mario.com.Models.CurrentWeather
import mario.com.Models.Day
import mario.com.Models.Hour
import mario.com.R
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var tempTextView: TextView
    private lateinit var precipTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var iconImageView: ImageView
    private lateinit var mainActivity: View
    private lateinit var currentTimetextView:TextView
    private var latitud = 37.8267
    private var longitud = -122.4233

    var days:ArrayList<Day> = ArrayList()
    var hours:ArrayList<Hour> = ArrayList()
    companion object{
        val DAYLY_WEATHER="DAYLY_WEATHER"
        val HOURLY_WEATHER="HOURLY_WEATHER"
    }

    private var PERMISSION_ID=1000
    //Now we need to create some variables that we will need
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRquest:LocationRequest

    private lateinit var localizationTextView: TextView
    private val locationCallback=object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult?) {
            var lastLocation= p0?.lastLocation
            //Now we will set the new Location
//            locationTextView.text="Your Current Location is, Lat:${lastLocation?.latitude} & Lon:${lastLocation?.longitude}"
            latitud= lastLocation?.latitude!!
            longitud=lastLocation?.longitude!!
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
        tempTextView= findViewById(R.id.tempTextView)
        precipTextView= findViewById(R.id.precipTextView)
        descriptionTextView= findViewById(R.id.descriptionTextView)
        iconImageView=findViewById(R.id.iconImageView)
        mainActivity=findViewById(R.id.main)
        currentTimetextView=findViewById(R.id.currentTimetextView)
        localizationTextView=findViewById(R.id.localizationTextView)
        tempTextView.text= getString(R.string.temp_placeholder, 0)
        precipTextView.text= getString(R.string.prcip_placeholder, 0)
        currentTimetextView.text=getString(R.string.currentTime_placeholder, "")
        getWeather(latitud,longitud)
    }

    private fun getWeather(latitud:Double,longitud:Double) {
//        val latitud = 37.8267
//        val longitud = -122.4233
        val language = getString(R.string.language)
        val units = getString(R.string.units)
        val url = "$DARCK_SKY_URL/$API_KEY/$latitud,$longitud?lang=$language&units=$units"

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
                Request.Method.GET, url,
                {
                    //1.-Obtener nuestro clima actual con la clase JSONParser
                    //2.- Asignar los valores a las views adecuadas
                    val responseJSON = JSONObject(it)
                    val currentWeather = getCurrentWeatherFromJson(responseJSON)
                    days = getDailyWeatherFromJson(responseJSON)
                    hours = getHourlyWeatherFromJson(responseJSON)
                    buildCurrentWeatherUI(currentWeather)
                },
                {
                    displayErrorMessage(mainActivity)
                })
        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    private fun buildCurrentWeatherUI(currentWeather: CurrentWeather) {
        with(currentWeather){
            tempTextView.text= getString(R.string.temp_placeholder, temp.toInt())
            precipTextView.text= getString(R.string.prcip_placeholder, (precip * 100).toInt())
            descriptionTextView.text= summary
            iconImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, getIconResource(), null))
            currentTimetextView.text=getString(R.string.currentTime_placeholder, time)
        }
    }

    fun displayErrorMessage(view: View){
        mainActivity.displaySnack(getString(R.string.network_error), Snackbar.LENGTH_INDEFINITE){
            action(getString(R.string.retry)){
                getWeather(latitud,longitud)
            }
        }
    }

    fun startHourlyActivity(view: View){
        val intent= Intent(this, HourlyWeatherActivity::class.java).apply {
            putParcelableArrayListExtra(HOURLY_WEATHER, hours)
        }
        startActivity(intent)
    }

    fun startDailyActivity(view: View){
        //Toast.makeText(this,"Start Daily activity",Toast.LENGTH_LONG).show()
        //agregar en una sola accion de apply
        val intent= Intent(this, DailyWeatherActivity::class.java).apply {
            putParcelableArrayListExtra(DAYLY_WEATHER, days)
        }
        startActivity(intent)
    }

    //First we need to create a function thath will check the uses permission
    private fun CheckPermission():Boolean{
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    //Now we need to create a function that will allow us to get user permission
    private fun RequestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),PERMISSION_ID)
    }

    //Now er need a function that check if the location service of the device is enabled
    private fun IsLocationEnabled():Boolean{
        var locationManager: LocationManager =getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)|| locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    //Function to get country & city name
    private fun getFullLocaitonName(lat:Double,long: Double):String{
        var cityName=""
        var countryName=""
        var address= Geocoder(this, Locale.getDefault()).getFromLocation(lat,long,1)
        cityName= address[0].locality
        countryName= address[0].countryName
        return "$cityName, $countryName"
    }

    //Now we will create a function that will allow us to get the last location
    private fun getLastLocation(){
        //First we check permission
        if(CheckPermission()){
            //Now we check the location service is enable
            if(IsLocationEnabled()) {
                //Now let´s get location
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(){ task->
                    var location: Location? = task.result
                    if(location==null){
                        getNewLocation()
                    }else{
//                        locationTextView.text="Your Current Location is, Lat:${location.latitude} & Lon:${location.longitude} \n${getFullLocaitonName(location.latitude,location.longitude)}"
                        localizationTextView.text=getFullLocaitonName(location.latitude,location.longitude)
                    }
                }
            }else{
                Toast.makeText(this,"Please Enable you Location Service", Toast.LENGTH_SHORT).show()
            }
        }else{
            RequestPermission()
        }
    }

    private fun getNewLocation(){
        locationRquest= LocationRequest()
        locationRquest.priority= LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRquest.interval=0
        locationRquest.fastestInterval=0
        locationRquest.numUpdates=2
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRquest,locationCallback, Looper.myLooper()
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //This is a buil in function that check the permission result
        if(requestCode==PERMISSION_ID){
            if(grantResults.isEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Log.d("Debug","You have Permission")
            }
        }
    }
}