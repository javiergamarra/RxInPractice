package com.nhpatt.rxjava.basic;

import com.nhpatt.rxjava.GitHubService;
import org.junit.Before;
import org.junit.Test;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.observers.TestSubscriber;

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
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        service = retrofit.create(GitHubService.class);

        testSubscriber = new TestSubscriber<>();
    }

    @Test
    public void observablesEmitThings() {

        Observable.just("Hi!").subscribe(testSubscriber);
        List<Object> dataEmitted = testSubscriber.getOnNextEvents();

        assertThat(dataEmitted, hasSize(equalTo(___)));
        assertThat(dataEmitted, containsInAnyOrder(___));
    }

    @Test
    public void observablesEmitSeveralItems() {

        List<String> severalThings = Arrays.asList("1", "2");

        Observable.from(severalThings).subscribe(testSubscriber);
        List<Object> onNextEvents = testSubscriber.getOnNextEvents();

        assertThat(onNextEvents, hasSize(equalTo(___)));
        assertThat(onNextEvents, containsInAnyOrder(___, ___));


        testSubscriber = new TestSubscriber<>();
        Observable.just(severalThings).subscribe(testSubscriber);
        List<Object> onJustNextEvents = testSubscriber.getOnNextEvents();

        assertThat(onJustNextEvents, hasSize(equalTo(___)));
    }

    @Test
    public void networkCallsCanBeObservables() {

        service.listRepos(____).subscribe(testSubscriber);
        List<Object> dataEmitted = testSubscriber.getOnNextEvents();

        assertThat(dataEmitted, is(not(empty())));
    }

}
