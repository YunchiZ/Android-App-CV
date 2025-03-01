package com.team13.whatsmissing.di;

import android.content.Context;
import android.util.Log;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

/**
 * Hilt module for providing OpenCvs BaseLoaderCallback which is used
 * for OpenCV initialisation, means it does not have to be initialised in
 * a fragment / activity since Hilt can provide the applications context itself
 */
@Module
@InstallIn(SingletonComponent.class)
abstract class OpenCvModule {

    @Provides
    @Singleton
    static BaseLoaderCallback provideBaseLoaderCallback(
            @ApplicationContext Context app
    ) {

        return new BaseLoaderCallback(app) {
            @Override
            public void onManagerConnected(int status) {
                if (status == LoaderCallbackInterface.SUCCESS) {
                    Log.v("OpenCvModule", "OpenCV loaded successfully");
                } else {
                    super.onManagerConnected(status);
                }
            }
        };

    }


}
