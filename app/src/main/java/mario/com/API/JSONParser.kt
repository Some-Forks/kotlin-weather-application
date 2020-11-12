package mario.com.API

import mario.com.Extension.iterator
import mario.com.Models.CurrentWeather
import mario.com.Models.Day
import mario.com.Models.Hour
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


fun getCurrentWeatherFromJson(response: JSONObject):CurrentWeather{
val currentJSON=response.getJSONObject(currently)
    with(currentJSON){
        return CurrentWeather(
                icon = getString(icon),
                summary = getString(summary),
                temp = getDouble(temperature),
                precip = getDouble(precipProbability),
                time = formatCurrentTime(getLong(time))
        )
    }
}

fun getDailyWeatherFromJson(response: JSONObject):ArrayList<Day>{
    val dailyJSOON=response.getJSONObject(daily)
    val timeZone=response.getString(timeZone)
    val dayJSONArray=dailyJSOON.getJSONArray(data)
    val days= ArrayList<Day>()

    for(jsonDay in dayJSONArray){
        with(jsonDay){
            val minTemp=getDouble(temperatureMin)
            val maxTemp=getDouble(temperatureMax)
            val time=getLong(time)
            days.add(Day(time, minTemp, maxTemp, timeZone))
        }

    }
    return days
}

fun getHourlyWeatherFromJson(response: JSONObject):ArrayList<Hour>{
    val hourlyJSOON=response.getJSONObject(hourly)
    val timeZone=response.getString(timeZone)
    val hourJSONArray=hourlyJSOON.getJSONArray(data)
    val hours= ArrayList<Hour>()

    for(jsonHour in hourJSONArray){
        with(jsonHour){
            val time =getLong(time)
            val temperature=getDouble(temperature)
            val precipProb=getDouble(precipProbability)
            hours.add(Hour(time, temperature, precipProb, timeZone))
        }
    }
    return hours
}

fun formatCurrentTime(time: Long):String{

    val date: Date = Date(time * 1000L)
    val sdf = SimpleDateFormat("HH:mm dd/MM/yyyy")
    sdf.timeZone = TimeZone.getTimeZone(timeZone)
    return sdf.format(date)
}