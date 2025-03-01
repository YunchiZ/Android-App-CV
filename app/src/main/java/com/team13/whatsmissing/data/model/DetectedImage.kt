package com.team13.whatsmissing.data.model

import android.graphics.Bitmap

data class DetectedImage(
    val image: Bitmap,
    val label: String
)
