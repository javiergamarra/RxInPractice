package com.nhpatt.rxjava;

import org.junit.Before;
import org.junit.Test;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.util.Arrays;
import java.util.List;

public class RxWorkshop {

    private Retrofit retrofit;
    private GitHubService service;

    @Before
    public void setUp() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
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
