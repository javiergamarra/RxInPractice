package com.nhpatt.rxjava;

import org.junit.Test;
import rx.Observable;

public class RxIsALibrary {

    @Test
    public void anObservableEmitsThings() {
        Observable.just("Hi!")
                .subscribe(System.out::println, thr -> System.err.println(thr.getMessage()), () -> System.out.println("finished!"));
    }

}
