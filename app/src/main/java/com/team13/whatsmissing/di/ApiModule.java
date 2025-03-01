package com.team13.whatsmissing.di;

import static com.team13.whatsmissing.common.Constant.ASSOCIATIONS_URL;
import static com.team13.whatsmissing.common.Constant.BASE_URL;

import com.team13.whatsmissing.data.network.AssociationsApiService;
import com.team13.whatsmissing.data.network.WhatsMissingApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Hilt module responsible for building and providing the API service
 */
@Module
@InstallIn(SingletonComponent.class)
public class ApiModule {

    @Provides
    @Singleton
    static WhatsMissingApiService whatsMissingApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        return retrofit.create(WhatsMissingApiService.class);
    }

    @Provides
    @Singleton
    static AssociationsApiService associationApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ASSOCIATIONS_URL)
                .build();
        return retrofit.create(AssociationsApiService.class);
    }


}
