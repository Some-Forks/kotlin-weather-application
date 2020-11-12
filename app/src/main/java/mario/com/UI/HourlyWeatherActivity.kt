package mario.com.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mario.com.Adapters.HourAdapter
import mario.com.Models.Hour
import mario.com.R

class HourlyWeatherActivity : AppCompatActivity() {

    private lateinit var hourlyRecyclerView:RecyclerView
    private lateinit var emptyTextView: TextView
    //Crear la clase Hour
    //Modificar JSONParser para obtener Hour
    //Hacer Hour Parceable
    //Mandar arreglo de horas de MainActivity a HourlyWeatherActivity
    //Crear nuestro adapter
    //Desplegar informaion en nuestra UI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hourly_weather)
        hourlyRecyclerView=findViewById(R.id.hourlyRecyclerView)
        emptyTextView=findViewById(R.id.emptyTextView)

        hourlyRecyclerView.layoutManager=LinearLayoutManager(this)

        intent.let {
            //Desplegar informacion de la lista
            val hours:ArrayList<Hour> = it.getParcelableArrayListExtra(MainActivity.HOURLY_WEATHER)!!

            if (hours.isEmpty()) {
                hourlyRecyclerView.visibility = View.GONE;
                emptyTextView.visibility = View.VISIBLE;
            }
            else {
                hourlyRecyclerView.visibility = View.VISIBLE;
                hourlyRecyclerView.adapter=HourAdapter(hours)
                emptyTextView.visibility = View.GONE;
            }
        }


    }
}