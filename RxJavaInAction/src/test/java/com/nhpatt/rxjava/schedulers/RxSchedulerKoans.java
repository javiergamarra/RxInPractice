package com.nhpatt.rxjava.schedulers;

import com.nhpatt.rxjava.GitHubService;
import com.nhpatt.rxjava.Repo;
import org.junit.Before;
import org.junit.Test;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Scheduler;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;

public class RxSchedulerKoans {

    private GitHubService service;
    private TestSubscriber testSubscriber;

    private String ____;
    private static final Scheduler _____ = null;

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
    public void schedulersAllowControllingTheThread() {
        service.listRepos(____)
                .subscribeOn(_____)
                .observeOn(Schedulers.io())
                .subscribe(testSubscriber);

        List<Repo> onNextEvents = testSubscriber.getOnNextEvents();

        assertThat(onNextEvents, is(not(empty())));
    }
}
