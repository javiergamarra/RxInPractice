package com.nhpatt.rxjava.koans;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
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
    private TestObserver testObserver;

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

        testObserver = new TestObserver();
    }

    @Test
    public void observablesEmitThings() {

        Maybe.just("Hi!").subscribe(testObserver);
        List<Object> dataEmitted = testObserver.values();

        assertThat(dataEmitted, hasSize(equalTo(___)));
        assertThat(dataEmitted, containsInAnyOrder(___));
    }

    @Test
    public void observablesEmitSeveralItems() {

        List<String> severalThings = Arrays.asList("1", "2");

        Observable.fromIterable(severalThings).subscribe(testObserver);
        List<Object> onNextEvents = testObserver.values();

        assertThat(onNextEvents, hasSize(equalTo(___)));
        assertThat(onNextEvents, containsInAnyOrder(___, ___));


        testObserver = new TestObserver();
        Observable.just(severalThings).subscribe(testObserver);
        List<Object> onJustNextEvents = testObserver.values();

        assertThat(onJustNextEvents, hasSize(equalTo(___)));
    }

    @Test
    public void networkCallsCanBeObservables() {

        service.listRepos(____).subscribe(testObserver);
        List<Object> dataEmitted = testObserver.values();

        assertThat(dataEmitted, is(not(empty())));
    }

}
