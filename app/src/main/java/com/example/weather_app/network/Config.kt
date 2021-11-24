package com.example.weather_app.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class Config {
    fun getRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getService() = getRetrofit().create(ModelWeather::class.java)


}

interface ModelWeather {
    @GET("weather")
    fun getModelWeather(@Query("lat") lat:String,
                        @Query("lon") lon:String,
                        @Query("appid") appid:String): Call<WeatherModel>
}