package mario.com.Adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mario.com.Extension.inflate
import mario.com.Models.Hour
import mario.com.R

class HourAdapter( val days:ArrayList<Hour>):RecyclerView.Adapter<HourAdapter.HourViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourViewHolder
            =HourViewHolder(parent.inflate(R.layout.hourly_item))


    override fun onBindViewHolder(holder: HourViewHolder, position: Int) =holder.bind(days[position])


    override fun getItemCount(): Int = days.size


    class HourViewHolder(hourlyItemView:View):RecyclerView.ViewHolder(hourlyItemView){
        lateinit var hourTextView:TextView
        lateinit var hourPrompTextView:TextView
        lateinit var hourTempTextView: TextView

        fun bind(hour:Hour)= with(itemView){
            hourTextView= findViewById(R.id.hourTextView)
            hourPrompTextView= findViewById(R.id.hourPrompTextView)
            hourTempTextView= findViewById(R.id.hourTempTextView)

            hourTextView.text=hour.getFormattedTime()
            hourPrompTextView.text="${hour.precip.toInt()} %"
            hourTempTextView.text="${hour.temp.toInt()} C"
        }
    }
}