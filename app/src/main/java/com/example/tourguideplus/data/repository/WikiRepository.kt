package com.example.tourguideplus.data.repository

import com.example.tourguideplus.data.network.WikiNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WikiRepository {
    suspend fun fetchExtract(title: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val resp = WikiNetwork.api.getExtract(title)
            // берём первый page из map
            val extract = resp.query?.pages?.values?.firstOrNull()?.extract
            Result.success(extract.orEmpty())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}