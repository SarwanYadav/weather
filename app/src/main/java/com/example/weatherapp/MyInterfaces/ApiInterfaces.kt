package com.example.weatherapp.MyInterfaces

import com.example.weatherapp.model.WeatherApp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterfaces {

    @GET("weather")

    fun getWeatherdata(
        @Query("q") city: String, @Query("appid") appid: String, @Query("units") units: String
    ): Call<WeatherApp>

}