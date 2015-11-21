package com.nhpatt.rxjava;

import org.junit.Test;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;

import java.util.Arrays;
import java.util.List;

public class RxIsALibrary {

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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        GitHubService service = retrofit.create(GitHubService.class);
        service.listRepos("nhpatt").subscribe(System.out::println);
    }

}
