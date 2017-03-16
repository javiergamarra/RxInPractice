package com.nhpatt.rxjava;

import io.reactivex.annotations.NonNull;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RxWorkshop {

    private TalksService service;

    @Before
    public void setUp() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://data.agenda.wedeploy.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        service = retrofit.create(TalksService.class);
    }

    @Test
    public void anObservableEmitsThings() {
    }

    @Test
    public void anObservableEmitsSeveralThings() {
    }

    @Test
    public void anObservableCanFail() {
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
    public void retrievingListAndDetail() {
    }

    @Test
    public void schedulersAllowControllingTheThread() {
    }

    @Test
    public void observablesAreLazy() {
    }

    private void printBestTalk(@NonNull Talk talk) {
        System.out.println("Best talk: " + talk);
    }

    private boolean speakerIsAmalia(Talk talk) {
        return talk.getSpeaker().contains("amalia");
    }

    private boolean isAGoodTalk(Talk talk) {
        return talk.getScore() >= 3;
    }
}
