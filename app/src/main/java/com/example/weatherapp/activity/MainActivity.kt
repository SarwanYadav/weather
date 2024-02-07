package com.example.weatherapp.activity

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.weatherapp.MyInterfaces.ApiInterfaces
import com.example.weatherapp.R
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


    // Add SearchView funcation work


    private fun SearchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    featWeaterData(query)

                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true

            }

        })
    }


    // create method get data and call APi use retrofit

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
                    val Sunrise = responseBody.sys.sunrise.toLong()
                    val sunset = responseBody.sys.sunset.toLong()
                    val sealevel = responseBody.main.pressure
                    val max = responseBody.main.temp_max
                    val min = responseBody.main.temp_min
                    val condition = responseBody.weather.firstOrNull()?.main ?: "unknow"


                    // binding data

                    binding.temp.text = "$temperature °C"
                    binding.weater.text = condition
                    binding.max.text = "Max Temp: $max °C"
                    binding.min.text = "Min Temp : $min°C"
                    binding.Humidity.text = "$humidity"
                    binding.windSpeed.text = "$windSpeed"
                    binding.Sunrise.text = "${time(Sunrise)}"
                    binding.sunset.text = "${time(sunset)}"
                    binding.sealevel.text = "$sealevel hPa"
                    binding.condition.text = "condition"


                    // set Date and day

                    binding.day.text = dayName(System.currentTimeMillis())
                    binding.date.text = date()

                    binding.cityName.text = "$cityName"


//                   Log.d("TAG", "onResponse: $temperature")


                    changeImageAccordingToweatherCondition(condition)

                }
            }

            override fun onFailure(call: Call<WeatherApp?>, t: Throwable) {
                Log.d(TAG, "Fail!")


            }
        })


    }


    private fun changeImageAccordingToweatherCondition(condition: String) {

        when (condition) {
            "Clear Sky", "Sunny", "Clear" -> {
                binding.root.setBackgroundColor(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)

            }

            "Party Clouds", "Clouds", "Overcast", "Mist", "Foggy" -> {

                binding.root.setBackgroundColor(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)

            }


            "Ligt Snow", "Moderate Snow", "Heavy Snow", "Blizzard" -> {
                binding.root.setBackgroundColor(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }

            else -> {
                binding.root.setBackgroundColor(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }


        }



        binding.lottieAnimationView.playAnimation()
    }

}


private fun date(): String {
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return sdf.format((Date()))

}

private fun time(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format((Date(timestamp * 1000)))

}


fun dayName(timestamp: Long): String {
    val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
    return sdf.format((Date()))

}










