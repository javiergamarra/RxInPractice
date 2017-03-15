package com.nhpatt.rxjava;

import org.junit.Before;
import org.junit.Test;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RxWorkshop {

    private Retrofit retrofit;
    private GitHubService service;

    @Before
    public void setUp() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        service = retrofit.create(GitHubService.class);
    }

    @Test
    public void anObservableEmitsThings() {
    }

    @Test
    public void anObservableEmitsSeveralThings() {
    }

    @Test
    public void aNetworkCallIsAnObservable() {
    }

    @Test
    public void mapTransformsEachElement() {
    }

    @Test
    public void mapDoesNotWorkWellWithLists() {
    }

    @Test
    public void flatmapCanReturnElementsFromAnObservable() {
    }

    @Test
    public void filteringResults() {
    }

    @Test
    public void accumulatingResults() {
    }

    @Test
    public void accumulatingResultsTheOldWay() {
    }

    @Test
    public void retrievingListAndDetail() {
    }

    @Test
    public void schedulersAllowControllingTheThread() {
    }
}
