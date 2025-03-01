package com.team13.whatsmissing.data.repository;

import android.util.Log;

import com.team13.whatsmissing.data.network.AssociationsApiService;
import com.team13.whatsmissing.data.network.model.AssociationsApiResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;

public class RemoteAssociationsRepository implements AssociationsRepository {

    AssociationsApiService api;

    @Inject
    RemoteAssociationsRepository(AssociationsApiService api) {
        this.api = api;
    }

    @Override
    public List<String> getAssociations(String word) {
        try {
            AssociationsApiResponse response = api.getSimilarObjects(word).execute().body();
            return response.getAssociationsSortedByScore();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

}
