package com.nhpatt.rxjava;

import org.junit.Before;
import org.junit.Test;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class RxBasicKoans {

    private GitHubService service;
    private TestSubscriber testSubscriber;

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

        assertThat(dataEmitted, hasSize(equalTo(1)));
        assertThat(dataEmitted, containsInAnyOrder("Hi!"));
    }

    @Test
    public void observablesEmitSeveralItems() {

        List<String> severalThings = Arrays.asList("1", "2");

        Observable.from(severalThings).subscribe(testSubscriber);
        List<Object> onNextEvents = testSubscriber.getOnNextEvents();

        assertThat(onNextEvents, hasSize(equalTo(2)));
        assertThat(onNextEvents, containsInAnyOrder("2", "1"));


        testSubscriber = new TestSubscriber<>();
        Observable.just(severalThings).subscribe(testSubscriber);
        List<Object> onJustNextEvents = testSubscriber.getOnNextEvents();

        assertThat(onJustNextEvents, hasSize(equalTo(1)));
    }

    @Test
    public void networkCallsCanBeObservables() {

        service.listRepos("nhpatt").subscribe(testSubscriber);
        List<Object> dataEmitted = testSubscriber.getOnNextEvents();

        assertThat(dataEmitted, is(not(empty())));
    }

    @Test
    public void mapTransformsEachElement() {
        List<String> severalThings = Arrays.asList("1", "2");

        Observable.from(severalThings)
                .map(Integer::valueOf)
                .subscribe(testSubscriber);

        List<Object> onNextEvents = testSubscriber.getOnNextEvents();

        assertThat(onNextEvents, contains(1, 2));
    }


    @Test
    public void mapDoesNotWorkWellWithLists() {

        service.listRepos("nhpatt")
                .map(Observable::from)
                .subscribe(testSubscriber);

        List<Object> onNextEvents = testSubscriber.getOnNextEvents();

        assertThat(onNextEvents, hasSize(equalTo(1)));
    }


    @Test
    public void flatmapCanReturnAnyNumberOfElements() {

        service.listRepos("nhpatt")
                .flatMap(Observable::from)
                .map(Repo::getName)
                .map((s) -> s.replace("-", " "))
                .subscribe(testSubscriber);

        List<Object> onNextEvents = testSubscriber.getOnNextEvents();

        assertThat(onNextEvents, hasSize(equalTo(30)));
    }

    @Test
    public void filterRemovesElementsByACondition() {

        service.listRepos("nhpatt")
                .flatMap(Observable::from)
                .map(Repo::getName)
                .map((s) -> s.replace("-", " "))
                .filter((s) -> s.startsWith("Android"))
                .subscribe(testSubscriber);

        List<Object> onNextEvents = testSubscriber.getOnNextEvents();

        assertThat(onNextEvents, hasSize(equalTo(5)));
    }

    @Test
    public void takePickTheFirstNElements() {

        service.listRepos("nhpatt")
                .flatMap(Observable::from)
                .map(Repo::getName)
                .map((s) -> s.replace("-", " "))
                .filter((s) -> s.startsWith("Android"))
                .take(2)
                .subscribe(testSubscriber);

        List<Object> onNextEvents = testSubscriber.getOnNextEvents();

        assertThat(onNextEvents, hasSize(equalTo(2)));
    }

    @Test
    public void scanPassTheValueToTheNextResult() {

        service.listRepos("nhpatt")
                .flatMap(Observable::from)
                .map(Repo::getName)
                .map((s) -> s.replace("-", " "))
                .filter((s) -> s.startsWith("Android"))
                .take(2)
                .map(String::length)
                .scan((x, y) -> x * y)
                .subscribe(testSubscriber);

        List<Object> onNextEvents = testSubscriber.getOnNextEvents();

        assertThat(onNextEvents, is(not(empty())));
        assertThat(onNextEvents, contains(7, 119));
    }

    @Test
    public void accumulateWithoutScanIsVerbose() {

        service.listRepos("nhpatt")
                .map(
                        (l) -> {
                            int result = 0;
                            int i = 0;
                            int oldLength = 1;
                            List<Integer> values = new ArrayList<Integer>();

                            for (Repo repo : l) {
                                String name = repo.getName();
                                String replacedName = name.replace("-", " ");

                                if (replacedName.startsWith("Android") && i < 2) {
                                    result = replacedName.length() * oldLength;
                                    oldLength = result;
                                    System.out.println(result);
                                    i++;
                                    values.add(result);
                                } else if (i > 2) {
                                    break;
                                }

                            }
                            return values;
                        }
                )
                .flatMap(integers -> Observable.from(integers))
                .subscribe(testSubscriber);

        List<Object> onNextEvents = testSubscriber.getOnNextEvents();

        assertThat(onNextEvents, is(not(empty())));
        assertThat(onNextEvents, contains(7, 119));
    }

    @Test
    public void mergeJoinsTwoStreams() {

        Observable<Repo> repos = service.listRepos("nhpatt")
                .flatMap(Observable::from);

        Observable<Repo> goodRepos = service.listRepos("pedrovgs")
                .flatMap(Observable::from);

        Observable.merge(repos, goodRepos).subscribe(testSubscriber);

        List<Object> onNextEvents = testSubscriber.getOnNextEvents();

        assertThat(onNextEvents, is(not(empty())));
        assertThat(onNextEvents, hasSize(56));
    }

    @Test
    public void zipJoinsTwoStreamsByPosition() {

        Observable<Repo> repo = service.listRepos("nhpatt")
                .flatMap(Observable::from)
                .take(1);

        Observable<Commit> commit = service.listCommits("nhpatt", "Android")
                .flatMap(Observable::from)
                .take(1);

        Observable.zip(repo, commit, this::updateCommit).subscribe(testSubscriber);

        List<Repo> onNextEvents = testSubscriber.getOnNextEvents();

        assertThat(onNextEvents, is(not(empty())));
        assertThat(String.valueOf(onNextEvents.get(0).getCommit()), not(isEmptyOrNullString()));
    }

    private Repo updateCommit(Repo o, Commit o2) {
        o.setCommit(o2);
        return o;
    }

    @Test
    public void schedulersAllowControllingTheThread() {
        service.listRepos("nhpatt")
                .subscribeOn(Schedulers.immediate())
                .observeOn(Schedulers.io())
                .subscribe(System.out::println);

    }
}
