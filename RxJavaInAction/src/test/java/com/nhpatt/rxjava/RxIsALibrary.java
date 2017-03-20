package com.nhpatt.rxjava;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

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

        TestObserver<String> testObserver = Observable.just("Hola UVa!").test();

        testObserver.assertTerminated();
        testObserver.assertNoErrors();
        testObserver.assertValue("Hola UVa!");
    }

    @Test
    public void observablesCanEmitSeveralThings() {

        String[] severalThings = {"one", "two"};

        TestObserver<String> testObserver = Observable.fromArray(severalThings).test();

        testObserver.assertTerminated();
        testObserver.assertNoErrors();
        testObserver.assertValueCount(2);
    }

    @Test
    public void observablesCanFail() {

        TestObserver<Object> testObserver = Observable.fromCallable(() -> {
            throw new Exception("Fail!");
        }).test();

        testObserver.assertTerminated();
        testObserver.assertErrorMessage("Fail!");
    }

    @Test
    public void observablesCanRecoverFromAnError() {

        TestObserver<String> testObserver = Observable.fromCallable(() -> {
            if (new Random().nextBoolean()) {
                throw new Exception("2");
            }
            return "Hola!";
        }).onErrorReturnItem("Adios").test();

        testObserver.assertTerminated();
        testObserver.assertNoErrors();
    }

    @Test
    public void observablesCanBeCreatedFromANetworkCall() {

        TestObserver<List<Talk>> testObserver = service.listTalks().test();

        testObserver.assertTerminated();
        testObserver.assertNoErrors();
        assertThat(testObserver.values(), is(not(empty())));
    }


    @Test
    public void observablesThatReturnListsCanBeMappedToGetTheSize() {

        TestObserver<Integer> testObserver = service.listTalks()
                .map(List::size)
                .test();

        testObserver.assertTerminated();
        testObserver.assertNoErrors();
        testObserver.assertValueCount(1);
    }

    @Test
    public void observablesThatCreateObservablesCanNotBeMappedEasily() {

        TestObserver<Observable<Talk>> testObserver = service.listTalks()
                .map(Observable::fromIterable)
                .test();

        testObserver.assertTerminated();
        testObserver.assertNoErrors();
        testObserver.assertValueCount(1);
    }

    @Test
    public void observablesOfObservablesCanBeFlatmappedToGetTheUnderlyingObject() {

        TestObserver<Talk> testObserver = service.listTalks()
                .flatMapObservable(Observable::fromIterable)
                .test();

        testObserver.assertTerminated();
        testObserver.assertNoErrors();
        assertThat(testObserver.valueCount(), is(greaterThan(1)));
    }

    @Test
    public void observablesCanBeFiltered() {

        Observable<Long> scores = service.listTalks()
                .flatMapObservable(Observable::fromIterable)
                .map(Talk::getScore);
        Integer talkCount = scores.toList().map(List::size).blockingGet();

        TestObserver testObserver = scores.filter(x -> x >= 3)
                .test();

        testObserver.assertNoErrors();
        assertThat(testObserver.valueCount(), is(lessThan(talkCount)));
    }

    @Test
    public void observablesCanBeReduced() {

        TestObserver<Long> testObserver = service.listTalks()
                .flatMapObservable(Observable::fromIterable)
                .map(Talk::getScore)
                .filter(x -> x >= 3)
                .reduce((x, y) -> x + y)
                .test();

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValue(aLong -> aLong > 0);
    }

    @Test
    public void iteratorsCanAlsoReduce() {
        List<Talk> talks = service.listTalks().blockingGet();

        List<Long> scores = new ArrayList<>();
        for (Talk talk : talks) {
            scores.add(talk.getScore());
        }

        List<Long> filteredScores = new ArrayList<>();
        for (Long score : scores) {
            if (score >= 3) {
                filteredScores.add(score);
            }
        }

        Long sum = 0L;
        for (Long filteredScore : filteredScores) {
            sum += filteredScore;
        }

        assertThat(sum, is(greaterThan(0L)));
    }

    @Test
    public void iteratorsCanAlsoReduceInAnUglyWay() {
        List<Talk> talks = service.listTalks().blockingGet();

        Long sum = 0L;
        for (Talk talk : talks) {
            if (talk.getScore() >= 3) {
                sum += talk.getScore();
            }
        }

        assertThat(sum, is(greaterThan(0L)));
    }


    @Test
    public void retrievingListAndDetail() throws UnsupportedEncodingException {

        Single<List<Talk>> firstResults = service
                .filterTalks(getUrlParameters("TDD"))
                .map(SearchResult::getDocuments);
        Single<List<Talk>> secondResults = service
                .filterTalks(getUrlParameters("Reactive"))
                .map(SearchResult::getDocuments);

        TestSubscriber<List<Talk>> testSubscriber = Single.merge(firstResults, secondResults).test();
        testSubscriber.assertComplete();
        testSubscriber.assertNoErrors();
        assertThat(testSubscriber.valueCount(), is(greaterThan(1)));
    }

    private String getUrlParameters(String value) throws UnsupportedEncodingException {
        String query = "{\"*\": {\"operator\": \"fuzzy\", \"value\": {\"query\": \"" + value + "\"}}}";
        return URLEncoder.encode(query, "UTF-8");
    }

    @Test
    public void schedulersAllowControllingTheThread() {

        TestObserver<List<Talk>> testObserver = service.listTalks()
                .subscribeOn(Schedulers.trampoline())
                .observeOn(Schedulers.trampoline())
                .test();

        testObserver.assertNoErrors();
        assertThat(testObserver.valueCount(), is(greaterThan(0)));
    }

    @Test
    public void observablesAreLazy() {

        List<Talk> talks = service.listTalks().blockingGet();

        List<Talk> goodTalks = new ArrayList<>();
        for (Talk talk : talks) {
            System.out.println(talk);
            if (isAGoodTalk(talk) && speakerIsAmalia(talk)) {
                goodTalks.add(talk);
            }
        }

        printBestTalk(goodTalks.get(0));

        TestObserver<Talk> testObserver = service.listTalks()
                .toObservable()
                .flatMap(Observable::fromIterable)
                .doOnNext(System.out::println)
                .filter(this::isAGoodTalk)
                .filter(this::speakerIsAmalia)
                .firstElement()
                .test();

        testObserver.assertNoErrors();
        assertThat(testObserver.valueCount(), is(greaterThan(0)));
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
