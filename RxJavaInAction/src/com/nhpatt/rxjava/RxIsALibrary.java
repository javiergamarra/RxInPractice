package com.nhpatt.rxjava;

import org.junit.Test;
import rx.Observable;
import rx.Subscriber;

public class RxIsALibrary {

    @Test
    public void anObservableEmitsThings() {
        Observable<String> myObs = Observable.create(subscriber -> {
            subscriber.onNext("Hi!");
            subscriber.onCompleted();
        });

        Subscriber<String> mySubs = new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        };

        myObs.subscribe(mySubs);
    }

}
