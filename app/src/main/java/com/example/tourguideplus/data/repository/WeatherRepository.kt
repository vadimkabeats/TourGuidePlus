package com.example.tourguideplus.data.repository

import com.example.tourguideplus.data.network.WeatherResponse
import com.example.tourguideplus.data.network.NetworkModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository {
    private val api = NetworkModule.weatherApi

    suspend fun fetchWeather(city: String): Result<WeatherResponse> =
        withContext(Dispatchers.IO) {
            try {
                val resp = api.getCurrentWeather(city)
                Result.success(resp)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}