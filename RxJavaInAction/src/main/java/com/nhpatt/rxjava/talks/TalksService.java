package com.nhpatt.rxjava.talks;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.*;

import java.util.List;

public interface TalksService {

    @GET("/talks")
    Single<List<Talk>> talks();

    @GET("/talks")
    Single<SearchResult> filter(@Query(value = "search", encoded = true) String search);

    @DELETE("/talks")
    Completable deleteAll();

    @POST("/talks")
    Completable add(@Body Talk talk);
}