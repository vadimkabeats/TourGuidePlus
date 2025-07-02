package com.example.tourguideplus.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    private const val BASE_URL = "https://api.openweathermap.org/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val weatherApi: OpenWeatherApi by lazy {
        retrofit.create(OpenWeatherApi::class.java)
    }
}
object WikiNetwork {
    private const val BASE_URL = "https://en.wikipedia.org/"

    val api: WikiApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WikiApi::class.java)
    }
}