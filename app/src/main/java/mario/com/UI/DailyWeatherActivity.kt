package mario.com.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import mario.com.Adapters.DayAdapter
import mario.com.Models.Day
import mario.com.R

class DailyWeatherActivity : AppCompatActivity() {

    //Crear la clase Day
    //Modificar JSONParser para obtener Days
    //Hacer Day Parceable
    //Mandar arreglo de dias de MainActivity a DailyWeatherActivity
    //Crear nuestro adapter
    //Desplegar informaion en nuestr a UI

    private lateinit var dailyListView: ListView
    private lateinit var emptyTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_weather)

        dailyListView= findViewById(R.id.dailyListView)
        emptyTextView=findViewById(R.id.emptyTextView)
        intent.let {
            //Desplegar informacion de la lista
            val days:ArrayList<Day> = it.getParcelableArrayListExtra(MainActivity.DAYLY_WEATHER)!!
            val baseAdapter=DayAdapter(this,days)
            dailyListView.adapter=baseAdapter
        }
        dailyListView.emptyView=emptyTextView
    }
}