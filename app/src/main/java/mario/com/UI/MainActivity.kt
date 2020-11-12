package mario.com.UI

import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
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

    var days:ArrayList<Day> = ArrayList()
    var hours:ArrayList<Hour> = ArrayList()
    companion object{
        val DAYLY_WEATHER="DAYLY_WEATHER"
        val HOURLY_WEATHER="HOURLY_WEATHER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tempTextView= findViewById(R.id.tempTextView)
        precipTextView= findViewById(R.id.precipTextView)
        descriptionTextView= findViewById(R.id.descriptionTextView)
        iconImageView=findViewById(R.id.iconImageView)
        mainActivity=findViewById(R.id.main)
        currentTimetextView=findViewById(R.id.currentTimetextView)

        tempTextView.text= getString(R.string.temp_placeholder, 0)
        precipTextView.text= getString(R.string.prcip_placeholder, 0)
        currentTimetextView.text=getString(R.string.currentTime_placeholder, "")
        getWeather()
    }

    private fun getWeather() {
        val latitud = 37.8267
        val longitud = -122.4233
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
                getWeather()
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
}