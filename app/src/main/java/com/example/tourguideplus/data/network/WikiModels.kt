package com.example.tourguideplus.data.network

import com.google.gson.annotations.SerializedName

data class WikiExtractResponse(
    @SerializedName("query") val query: WikiQuery?
)

data class WikiQuery(
    @SerializedName("pages") val pages: Map<String, WikiPage>
)

data class WikiPage(
    @SerializedName("extract") val extract: String?
)