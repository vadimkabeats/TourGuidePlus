package com.example.tourguideplus.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface WikiApi {
    @GET("w/api.php?format=json&action=query&prop=extracts&exintro=true&explaintext=true")
    suspend fun getExtract(
        @Query("titles") title: String
    ): WikiExtractResponse
}