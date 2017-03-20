package com.nhpatt.rxjava.talks;

import io.reactivex.annotations.NonNull;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RxIsALibrary {

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
    public void observablesEmitAnElement() {

    }

    @Test
    public void observablesCanEmitSeveralThings() {

    }

    @Test
    public void observablesCanFail() {

    }

    @Test
    public void observablesCanRecoverFromAnError() {

    }

    @Test
    public void observablesCanBeCreatedFromANetworkCall() {

    }


    @Test
    public void observablesThatReturnListsCanBeMappedToGetTheSize() {

    }

    @Test
    public void observablesThatCreateObservablesCanNotBeMappedEasily() {

    }

    @Test
    public void observablesOfObservablesCanBeFlatmappedToGetTheUnderlyingObject() {

    }

    @Test
    public void observablesCanBeFiltered() {

    }

    @Test
    public void observablesCanBeReduced() {

    }

    @Test
    public void iteratorsCanAlsoReduce() {

    }

    @Test
    public void iteratorsCanAlsoReduceInAnUglyWay() {

    }

    @Test
    public void retrievingListAndDetail() throws UnsupportedEncodingException {

    }

    @Test
    public void schedulersAllowControllingTheThread() {

    }

    @Test
    public void observablesAreLazy() {

    }

    private String getUrlParameters(String value) throws UnsupportedEncodingException {
        String query = "{\"*\": {\"operator\": \"fuzzy\", \"value\": {\"query\": \"" + value + "\"}}}";
        return URLEncoder.encode(query, "UTF-8");
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
