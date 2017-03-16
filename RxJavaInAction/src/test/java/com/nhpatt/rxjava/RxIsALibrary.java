package com.nhpatt.rxjava;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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
    public void anObservableEmitsThings() {
        TestObserver<String> test = Observable.just("Hola UVa!").test();

        test.assertTerminated();
        test.assertNoErrors();
        test.assertValue("Hola UVa!");
    }

    @Test
    public void anObservableEmitsSeveralThings() {
        String[] severalThings = {"1", "2"};

        TestObserver<String> test = Observable.fromArray(severalThings).test();

        test.assertTerminated();
        test.assertNoErrors();
        test.assertValueCount(2);
    }

    @Test
    public void anObservableCanFail() {
        TestObserver test = Observable.fromCallable(() -> {
            throw new Exception("Fail!");
        }).test();

        test.assertTerminated();
        test.assertErrorMessage("Fail!");
    }

    @Test
    public void weCanRecoverFromAnError() {
        TestObserver<String> test = Observable.fromCallable(() -> {
            if (new Random().nextBoolean()) {
                throw new Exception("2");
            }
            return "Hola!";
        }).onErrorReturnItem("Adios")
                .test();

        test.assertTerminated();
        test.assertNoErrors();
    }

    @Test
    public void aNetworkCallIsAnObservable() {

        TestObserver<List<Talk>> test = service.listTalks().test();

        test.assertTerminated();
        test.assertNoErrors();
        assertThat(test.values(), is(not(empty())));
    }


    @Test
    public void mapTransformsEachElement() {
        String[] severalThings = {"1", "2"};

        Observable.fromArray(severalThings)
                .map(Integer::valueOf)
                .subscribe(System.out::println);

    }

    @Test
    public void mapDoesNotWorkWellWithLists() {

        service.listTalks()
                .map(Observable::fromIterable)
                .subscribe(System.out::println, System.err::println);
    }

    @Test
    public void flatmapCanReturnElementsFromAnObservable() {

        service.allTalks()
                .flatMap(Observable::fromIterable)
                .subscribe(System.out::println);
    }

    @Test
    public void filteringResults() {

        service.allTalks()
                .flatMap(Observable::fromIterable)
                .map(Talk::getScore)
                .filter(x -> x >= 3)
                .take(2)
                .subscribe(System.out::println);
    }

    @Test
    public void accumulatingResults() {

        service.allTalks()
                .flatMap(Observable::fromIterable)
                .map(Talk::getScore)
                .reduce((x, y) -> x + y)
                .subscribe(System.out::println);
    }

    @Test
    public void accumulatingResultsTheOldWay() {

    }

    @Test
    public void retrievingListAndDetail() {
        Observable<Talk> someTalks = service.allTalks()
                .flatMap(Observable::fromIterable)
                .take(1);

        Observable<Talk> anotherTalks = service.allTalks()
                .flatMap(Observable::fromIterable)
                .take(1);

        Observable.merge(someTalks, anotherTalks).subscribe(System.out::println);

    }

    @Test
    public void schedulersAllowControllingTheThread() {

        service.allTalks()
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .subscribe(System.out::println);

    }

    @Test
    public void observablesAreLazy() {

        List<Talk> talks = service.listTalks().blockingGet();

        List<Talk> goodTalks = new ArrayList<>();
        for (Talk talk : talks) {
            System.out.println(talk);
            if (isAGoodTalk(talk)) {
                goodTalks.add(talk);
            }
        }

        printBestTalk(goodTalks.get(0));

        service.listTalks()
                .toObservable()
                .flatMap(Observable::fromIterable)
                .doOnNext(System.out::println)
                .filter(this::isAGoodTalk)
                .firstElement()
                .subscribe(this::printBestTalk);

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
