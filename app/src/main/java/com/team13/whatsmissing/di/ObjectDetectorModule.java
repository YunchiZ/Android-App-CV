package com.team13.whatsmissing.di;

import com.team13.whatsmissing.data.detector.ObjectDetector;
import com.team13.whatsmissing.data.detector.RemoteObjectDetector;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.scopes.ViewModelScoped;

/**
 * Hilt module for binding any dependencies on ObjectDetector to the
 * LocalObjectDetector implementation
 */
@Module
@InstallIn(ViewModelComponent.class)
abstract class ObjectDetectorModule {

    @Binds
    @ViewModelScoped
    public abstract ObjectDetector bindObjectDetector(
            // IMPORTANT changing the type to either RemoteObjectDetector or LocalObjectDetector
            // will specify whether to use the local YOLO model or remote API
            RemoteObjectDetector objectDetector
            // TODO create a repository class to mediate local and remote data sources
    );

}
