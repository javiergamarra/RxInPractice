package com.nhpatt.rxjava.operators;

import com.nhpatt.rxjava.Commit;
import com.nhpatt.rxjava.GitHubService;
import com.nhpatt.rxjava.Repo;
import org.junit.Before;
import org.junit.Test;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString;
import static org.junit.Assert.assertThat;

public class RxOperatorsKoans {

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
    public void mapTransformsEachElement() {
        List<String> severalThings = Arrays.asList(____, ____);

        Observable.from(severalThings)
                .map(Integer::valueOf)
                .subscribe(testSubscriber);

        List<Object> onNextEvents = testSubscriber.getOnNextEvents();

        assertThat(onNextEvents, contains(___, ___));
    }

    @Test
    public void mapDoesNotWorkWellWithLists() {

        service.listRepos("nhpatt")
                .map(Observable::from)
                .subscribe(testSubscriber);

        List<Object> onNextEvents = testSubscriber.getOnNextEvents();

        assertThat(onNextEvents, hasSize(equalTo(___)));
    }

    @Test
    public void flatmapCanReturnAnyNumberOfElements() {

        service.listRepos("nhpatt")
                .flatMap(Observable::from)
                .map(Repo::getName)
                .map((s) -> s.replace("-", " "))
                .subscribe(testSubscriber);

        List<Object> onNextEvents = testSubscriber.getOnNextEvents();

        assertThat(onNextEvents, hasSize(equalTo(___)));
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

        assertThat(onNextEvents, hasSize(equalTo(___)));
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

        assertThat(onNextEvents, hasSize(equalTo(___)));
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
        assertThat(onNextEvents, contains(___, ___));
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
        assertThat(onNextEvents, contains(___, ___));
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
        assertThat(onNextEvents, hasSize(___));
    }

    @Test
    public void zipJoinsTwoStreamsByPosition() {

        Observable<Repo> repo = service.listRepos(____)
                .flatMap(Observable::from)
                .take(1);

        Observable<Commit> commit = service.listCommits(____, "Android")
                .flatMap(Observable::from)
                .take(1);

        Observable.zip(repo, commit, this::updateCommit).subscribe(testSubscriber);

        List<Repo> onNextEvents = testSubscriber.getOnNextEvents();

        assertThat(onNextEvents, is(not(empty())));
        assertThat(String.valueOf(onNextEvents.get(___).getCommit()), not(isEmptyOrNullString()));
    }

    private Repo updateCommit(Repo o, Commit o2) {
        o.setCommit(o2);
        return o;
    }

}
