package mario.com.UI

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import mario.com.R
import java.util.*

class LocationActivity : AppCompatActivity() {

    //The permission id is just a int must be unique so you can use any number
    private var PERMISSION_ID=1000
    //Now we need to create some variables that we will need
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRquest:LocationRequest
    private lateinit var locationButton: Button
    private lateinit var locationTextView: TextView
    private val locationCallback=object :LocationCallback(){
        override fun onLocationResult(p0: LocationResult?) {
            var lastLocation= p0?.lastLocation
            //Now we will set the new Location
            locationTextView.text="Your Current Location is, Lat:${lastLocation?.latitude} & Lon:${lastLocation?.longitude}"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        locationTextView=findViewById(R.id.locationTextView)
        locationButton=findViewById(R.id.locationButton)

        //Now let´s initate the fusedLocationProviderClient
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
        locationButton.setOnClickListener {
            getLastLocation()
        }

    }
    //First we need to create a function thath will check the uses permission
    private fun CheckPermission():Boolean{
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    //Now we need to create a function that will allow us to get user permission
    private fun RequestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),PERMISSION_ID)
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
        var address=Geocoder(this, Locale.getDefault()).getFromLocation(lat,long,1)
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
                        locationTextView.text="Your Current Location is, Lat:${location.latitude} & Lon:${location.longitude} \n${getFullLocaitonName(location.latitude,location.longitude)}"
                    }
                }
            }else{
                Toast.makeText(this,"Please Enable you Location Service",Toast.LENGTH_SHORT).show()
            }
        }else{
            RequestPermission()
        }
    }

    private fun getNewLocation(){
        locationRquest= LocationRequest()
        locationRquest.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
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