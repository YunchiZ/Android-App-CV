package com.team13.whatsmissing.data.network;

import static com.team13.whatsmissing.common.Constant.ASSOCIATIONS_KEY;

import com.team13.whatsmissing.data.network.model.AssociationsApiResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AssociationsApiService {

    @Headers({
            "X-RapidAPI-Key: " + ASSOCIATIONS_KEY,
            "X-RapidAPI-Host: twinword-word-associations-v1.p.rapidapi.com"
    })
    @FormUrlEncoded
    @POST("associations/")
    Call<AssociationsApiResponse> getSimilarObjects(@Field("entry") String string);

}
