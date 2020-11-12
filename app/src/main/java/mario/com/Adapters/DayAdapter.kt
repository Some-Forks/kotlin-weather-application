package mario.com.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import mario.com.Extension.inflate
import mario.com.Models.Day
import mario.com.R

class DayAdapter(val context: Context, val datasource:ArrayList<Day>):BaseAdapter() {
    override fun getCount(): Int {
        return datasource.size
    }

    override fun getItem(position: Int): Any {
        return datasource[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, currentView: View?, parentView: ViewGroup): View {
        val viewHolder:ViewHolder
        val view:View
        if(currentView == null) {
            view = parentView.inflate(R.layout.daily_item)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }else{
            viewHolder= currentView?.tag as ViewHolder
            view=currentView
        }
        val currentDay=datasource[position]
        viewHolder.apply {
            dayTextView.text=currentDay.getFormattedTime()
            minTextView.text="Min ${currentDay.minTemp.toInt()} C"
            maxTextView.text="Min ${currentDay.maxTemp.toInt()} C"
        }
        return view
    }

    private class ViewHolder(view: View){
        val dayTextView:TextView=view.findViewById(R.id.dayTextView)
        val minTextView:TextView=view.findViewById(R.id.minTextView)
        val maxTextView:TextView=view.findViewById(R.id.maxTextView)
    }
}