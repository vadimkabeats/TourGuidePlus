package com.example.tourguideplus.data.network


import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("name") val cityName: String,
    @SerializedName("weather") val weather: List<WeatherDescription>,
    @SerializedName("main") val main: MainInfo,
    @SerializedName("wind") val wind: WindInfo,
    @SerializedName("dt") val timestamp: Long
)

data class WeatherDescription(
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class MainInfo(
    @SerializedName("temp") val temp: Double
)

data class WindInfo(
    @SerializedName("speed") val speed: Double
)