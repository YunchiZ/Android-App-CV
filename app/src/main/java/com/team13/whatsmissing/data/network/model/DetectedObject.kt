package com.team13.whatsmissing.data.network.model

/**
 * Data class used in the API response to store information about a single detected object
 */
data class DetectedObject(
    val right: Int,
    val bottom: Int,
    val top: Int,
    val score: Float,
    val id: String,
    val left: Int
)
