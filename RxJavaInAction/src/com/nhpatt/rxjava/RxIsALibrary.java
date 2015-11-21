package com.nhpatt.rxjava;

import org.junit.Test;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class RxIsALibrary {

    @Test
    public void anObservableEmitsThings() {
        Observable.just("Hi!").subscribe(System.out::println);
    }

}
