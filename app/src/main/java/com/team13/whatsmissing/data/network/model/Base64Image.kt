package com.team13.whatsmissing.data.network.model

import com.google.gson.annotations.SerializedName

/**
 * Data class for the API request
 */
data class Base64Image(
    @SerializedName("imageB64")
    val image: String
)