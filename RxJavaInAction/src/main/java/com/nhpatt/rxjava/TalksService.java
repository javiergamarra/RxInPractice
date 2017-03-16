package com.nhpatt.rxjava;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface TalksService {

    @GET("/talks")
    Single<List<Talk>> listTalks();

    @GET("/talks")
    Observable<List<Talk>> allTalks();

    @DELETE("/talks")
    Observable<Void> deleteTalks();

    @POST("/talks")
    Observable<Void> addTalk(@Body Talk talk);

//    http://data.boilerplate-data.wedeploy.io/tasks/?limit=5&sort=[{"id":"desc"}]
}