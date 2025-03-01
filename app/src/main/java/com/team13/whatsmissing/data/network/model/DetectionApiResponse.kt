package com.team13.whatsmissing.data.network.model

/**
 * Data class for storing the API response
 */
data class DetectionApiResponse(
    val statusCode: Int,
    val result: List<DetectedObject>
) {
    
    fun getObjectNames(): List<String> {
        return result.map { it.id }
    }

    fun getMostConfidentObject(): DetectedObject? {
        return result.maxByOrNull { it.score }
    }
    
}
