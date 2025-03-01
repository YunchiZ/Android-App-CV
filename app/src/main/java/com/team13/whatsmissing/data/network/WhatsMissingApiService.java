package com.team13.whatsmissing.data.network;

import com.team13.whatsmissing.data.network.model.DetectionApiResponse;
import com.team13.whatsmissing.data.network.model.Base64Image;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Interface for implementing the POST request to the API
 */
public interface WhatsMissingApiService {

    @Headers("Content-Type: application/json")
    @POST("image")
    Call<DetectionApiResponse> uploadImage(@Body Base64Image base64Image);

}
