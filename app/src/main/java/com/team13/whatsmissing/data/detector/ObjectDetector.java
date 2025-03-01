package com.team13.whatsmissing.data.detector;

import android.graphics.Bitmap;
import android.media.Image;

import com.team13.whatsmissing.data.model.DetectedImage;

import java.util.List;

import kotlin.Pair;

/**
 * Simple interface for ObjectDetectors
 * Means that local and remote models can be easily swapped out later
 */
public interface ObjectDetector {
    DetectedImage blurImage(Bitmap image);

}
