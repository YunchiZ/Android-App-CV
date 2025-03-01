package com.team13.whatsmissing.ui.capture;

import android.graphics.Bitmap;

import androidx.lifecycle.ViewModel;

import com.team13.whatsmissing.data.detector.ObjectDetector;
import com.team13.whatsmissing.data.model.DetectedImage;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlin.Pair;

/**
 * Responsible for remembering state during configuration changes
 * and interfacing with the data layer
 */
@HiltViewModel
public final class CaptureViewModel extends ViewModel {

    ObjectDetector detector;
    private DetectedImage blurredImage;

    @Inject
    CaptureViewModel(ObjectDetector detector) {
        this.detector = detector;
    }

    public DetectedImage blurObject(Bitmap image) {
        Thread detectionThread = new Thread(() -> blurredImage = detector.blurImage(image));
        detectionThread.start();
        try {
            detectionThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return blurredImage;
    }

}
