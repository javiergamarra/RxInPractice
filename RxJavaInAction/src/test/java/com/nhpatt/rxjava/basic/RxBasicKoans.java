package com.nhpatt.rxjava.basic;

import com.nhpatt.rxjava.GitHubService;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class RxBasicKoans {

    private GitHubService service;
    private TestSubscriber testSubscriber;

    private Integer ___;
    private String ____;

    @Before
    public void setUp() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        service = retrofit.create(GitHubService.class);

        testSubscriber = new TestSubscriber<>();
    }

    @Test
    public void observablesEmitThings() {

        Maybe.just("Hi!")
                .toFlowable()
                .subscribe(testSubscriber);
        List<Object> dataEmitted = testSubscriber.values();

        assertThat(dataEmitted, hasSize(equalTo(___)));
        assertThat(dataEmitted, containsInAnyOrder(___));
    }

    @Test
    public void observablesEmitSeveralItems() {

        List<String> severalThings = Arrays.asList("1", "2");

        Observable.fromIterable(severalThings)
                .toFlowable(BackpressureStrategy.BUFFER)
                .subscribe(testSubscriber);
        List<Object> onNextEvents = testSubscriber.values();

        assertThat(onNextEvents, hasSize(equalTo(___)));
        assertThat(onNextEvents, containsInAnyOrder(___, ___));


        testSubscriber = new TestSubscriber<>();
        Observable.just(severalThings)
                .toFlowable(BackpressureStrategy.BUFFER)
                .subscribe(testSubscriber);
        List<Object> onJustNextEvents = testSubscriber.values();

        assertThat(onJustNextEvents, hasSize(equalTo(___)));
    }

    @Test
    public void networkCallsCanBeObservables() {

        service.listRepos(____)
                .toFlowable(BackpressureStrategy.BUFFER)
                .subscribe(testSubscriber);
        List<Object> dataEmitted = testSubscriber.values();

        assertThat(dataEmitted, is(not(empty())));
    }

}
