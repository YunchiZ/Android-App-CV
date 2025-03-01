package com.team13.whatsmissing.data.detector;

import android.graphics.Bitmap;
import android.util.Base64;

import com.team13.whatsmissing.data.model.DetectedImage;
import com.team13.whatsmissing.data.network.WhatsMissingApiService;
import com.team13.whatsmissing.data.network.model.DetectionApiResponse;
import com.team13.whatsmissing.data.network.model.Base64Image;
import com.team13.whatsmissing.data.network.model.DetectedObject;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

/**
 * ObjectDetector implementation for API
 */
public class RemoteObjectDetector implements ObjectDetector {

    WhatsMissingApiService api;

    @Inject
    RemoteObjectDetector(WhatsMissingApiService api, BaseLoaderCallback openCvLoaderCallback) {
        this.api = api;
        if (OpenCVLoader.initDebug()) {
            openCvLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public DetectedImage blurImage(Bitmap image) {
        Base64Image encodedImage = new Base64Image(encodeToBase64(image, Bitmap.CompressFormat.JPEG, 50));
        try {
            DetectionApiResponse response = api.uploadImage(encodedImage).execute().body();
            DetectedObject object = response.getMostConfidentObject(); // TODO prompt to take another image if null
            Rect objectBox = new Rect(object.getLeft(), object.getTop(), Math.abs(object.getLeft() - object.getRight()), Math.abs(object.getTop() - object.getBottom()));
            Mat mat = new Mat();
            Utils.bitmapToMat(image, mat);
            Rect r = new Rect(0, 0, objectBox.width, objectBox.height);
            Imgproc.rectangle(mat.submat(objectBox), r, new Scalar(0), Imgproc.FILLED);
            //Imgproc.GaussianBlur(mat.submat(objectBox), mat.submat(objectBox), new Size(0, 0), 100);
            Utils.matToBitmap(mat, image);
            System.out.println(object.getId());
            return new DetectedImage(image, object.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

}
