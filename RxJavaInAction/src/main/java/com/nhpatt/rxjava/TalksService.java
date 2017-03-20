package com.nhpatt.rxjava;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.*;

import java.util.List;

public interface TalksService {

    @GET("/talks")
    Single<List<Talk>> listTalks();

    @GET("/talks")
    Single<SearchResult> filterTalks(@Query(value = "search", encoded = true) String search);

    @DELETE("/talks")
    Completable deleteTalks();

    @POST("/talks")
    Completable addTalk(@Body Talk talk);
}