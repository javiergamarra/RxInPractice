package com.nhpatt.rxjava;

import org.junit.Before;
import org.junit.Test;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;

import java.util.Arrays;
import java.util.List;

public class RxIsALibrary {

    private Retrofit retrofit;

    @Before
    public void setUp() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Test
    public void anObservableEmitsThings() {
        Observable.just("Hi!")
                .subscribe(System.out::println, thr -> System.err.println(thr.getMessage()), () -> System.out.println("finished!"));
    }

    @Test
    public void anObservableEmitsSeveralThings() {
        List<String> severalThings = Arrays.asList("1", "2");

        Observable.from(severalThings)
                .subscribe(System.out::println, thr -> System.err.println(thr.getMessage()), () -> System.out.println("finished!"));

    }

    @Test
    public void aNetworkCallIsAnObservable() {

        GitHubService service = retrofit.create(GitHubService.class);
        service.listRepos("nhpatt")
                .subscribe(System.out::println);
    }

    @Test
    public void mapTransformsEachElement() {
        List<String> severalThings = Arrays.asList("1", "2");

        Observable.from(severalThings)
                .map(Integer::valueOf)
                .subscribe(System.out::println);

    }

    @Test
    public void mapDoesNotWorkWellWithLists() {

        GitHubService service = retrofit.create(GitHubService.class);
        service.listRepos("nhpatt")
                .map(Observable::from)
                .subscribe(System.out::println);
    }

    @Test
    public void flatmapCanReturnElementsFromAnObservable() {

        GitHubService service = retrofit.create(GitHubService.class);
        service.listRepos("nhpatt")
                .flatMap(Observable::from)
                .subscribe(System.out::println);
    }

}
