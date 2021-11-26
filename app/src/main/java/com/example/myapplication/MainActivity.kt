package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

import com.example.myapplication.databinding.ActivityMainBinding
import okhttp3.*
import org.json.JSONObject
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        getData()

        binding.imageButtonFrance.setOnClickListener {
            val intent = Intent(this,FranceActivity::class.java)
            startActivity(intent)
        }
        binding.imageButtonEngland.setOnClickListener {
            val intent = Intent(this,EnglandActivity::class.java)
            startActivity(intent)
        }
        binding.imageButtonSwiss.setOnClickListener {
            val intent = Intent(this,SwissActivity::class.java)
            startActivity(intent)
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertUnixTimeToCurrent(unixTime : Int):String{
        val unixTimeInMilliSecond =  (unixTime * 1000).toLong()
        val date = Date(unixTimeInMilliSecond)
        val formatter = SimpleDateFormat("HH:mm aa")
        return formatter.format(date)

    }

    @SuppressLint("SetTextI18n")
    private fun getDataAndShowing(rawString : String ){
        val jsonObject = JSONObject(rawString)

        val name = jsonObject.getString("name")

        val weatherArray = jsonObject.getJSONArray("weather")
        val weatherObject = weatherArray.getJSONObject(0)
        val description = weatherObject.getString("description")

        val icon = weatherObject.getString("icon")
        val iconWeather = "https://openweathermap.org/img/wn/${icon}@2x.png"

        val sysObject = jsonObject.getJSONObject("sys")
        val sunriseInUnixTime = sysObject.getInt("sunrise") //second
        val sunsetInUnixTime = sysObject.getInt("sunset") //second

        val sunriseTime = convertUnixTimeToCurrent(sunriseInUnixTime)
        val sunsetTime = convertUnixTimeToCurrent(sunsetInUnixTime)

        val main = jsonObject.getJSONObject("main")
        val temp = main.getDouble("temp")
        val feelsLike = main.getDouble("feels_like")
        val tempMin = main.getDouble("temp_min")
        val tempMax = main.getDouble("temp_max")
        val pressure = main.getInt("pressure")
        val humidity = main.getInt("humidity")

        val coordinates  = jsonObject.getJSONObject("coord")
        val longitude = coordinates.getDouble("lon")
        val latitude = coordinates.getDouble("lat")



        runOnUiThread {
            binding.imageViewTower.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
            binding.textViewCityName.text = name
            binding.textViewCityDesc.text = description
            Glide.with(this@MainActivity).load(iconWeather).into(binding.imageViewIcon)
            binding.temp.text = " دما:  $temp"
            binding.feelsLike.text = " دمای احساس شده:  $feelsLike"
            binding.tempMin.text = " حداقل دما:  $tempMin"
            binding.tempMax.text = " حداکثر دما:  $tempMax"
            binding.pressure.text = "  فشار:  $pressure"
            binding.humidity.text = "  رطوبت:  $humidity"
            binding.textViewSunrise.text = sunriseTime
            binding.textViewSunset.text = sunsetTime
            binding.textViewLon.text = "  عرض جفرافیایی:$longitude "
            binding.textViewLat.text = "  طول جفرافیایی:$latitude "

        }
    }
    private fun getData(){
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://api.openweathermap.org/data/2.5/weather?q=tehran&appid=a1a3593bda00a8c6ef840d3f50446c8e&lang=fa&units=metric")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseRawString = response.body!!.string()
                getDataAndShowing(responseRawString)
            }
        })
    }
    fun reload(view : View){
        view as ImageView
        binding.imageViewTower.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE
        Glide.with(this@MainActivity).load(R.drawable.ic_baseline_refresh_24).into(binding.imageViewIcon)
        binding.textViewCityName.text = "..."
        binding.textViewCityDesc.text = "..."
        binding.temp.text = "..."
        binding.feelsLike.text = "..."
        binding.tempMin.text = "..."
        binding.tempMax.text = "..."
        binding.pressure.text = "..."
        binding.humidity.text = "..."
        binding.textViewSunrise.text = "..."
        binding.textViewSunset.text = "..."

        getData()
    }
}