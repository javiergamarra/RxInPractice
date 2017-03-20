package com.nhpatt.rxjava;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.*;

import java.util.List;

public interface TalksService {

    @GET("/talks")
    Single<List<Talk>> listTalks();

    @DELETE("/talks")
    Observable<Void> deleteTalks();

    @POST("/talks")
    Observable<Void> addTalk(@Body Talk talk);

    @GET("/talks")
    Single<List<Talk>> filterTalks(@Query(value = "search", encoded = true) String search);
}