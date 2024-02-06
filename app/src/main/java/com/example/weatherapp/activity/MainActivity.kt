package com.example.weatherapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.weatherapp.MyInterfaces.ApiInterfaces
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.model.WeatherApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// Api    https://api.openweathermap.org/data/2.5/weather?q=jaipur&appid=230aef08e9f53b65608119473a2ebeb8

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        featWeaterData("Jaipur")
        SearchCity()
    }

    private fun SearchCity() {

        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

        } {

        })


    }


    private fun featWeaterData(cityName: String) {


        val retrofit = Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiInterfaces::class.java)


        val response = retrofit.getWeatherdata(
            cityName, "230aef08e9f53b65608119473a2ebeb8", units = "metric"
        )

        response.enqueue(object : Callback<WeatherApp?> {
            override fun onResponse(call: Call<WeatherApp?>, response: Response<WeatherApp?>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val temperature = responseBody.main.temp.toString()
                    val humidity = responseBody.main.humidity
                    val windSpeed = responseBody.wind.speed
                    val Sunrise = responseBody.sys.sunrise
                    val sunset = responseBody.sys.sunset
                    val sealevel = responseBody.main.pressure
                    val max = responseBody.main.temp_max
                    val min = responseBody.main.temp_min
                    val Conditions = responseBody.weather.firstOrNull()?.main ?: "unknow"


                    // binding data

                    binding.temp.text = "$temperature °C"
                    binding.weater.text = Conditions
                    binding.max.text = "Max Temp: $max °C"
                    binding.min.text = "Min Temp : $min°C"
                    binding.Humidity.text = "$humidity"
                    binding.windSpeed.text = "$windSpeed"
                    binding.Sunrise.text = "$Sunrise"
                    binding.sunset.text = "$sunset"
                    binding.sealevel.text = "$sealevel hPa"
                    binding.condition.text = Conditions


                    binding.day.text = dayName(System.currentTimeMillis())
                    binding.date.text = date()

                    binding.cityName.text = "$cityName"


//                   Log.d("TAG", "onResponse: $temperature")


                }
            }

            override fun onFailure(call: Call<WeatherApp?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })


    }

    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))

    }

    fun dayName(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))

    }


}







