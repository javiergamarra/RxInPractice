package com.nhpatt.rxjava;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

import java.util.List;

public interface GitHubService {
    @GET("/users/{user}/repos")
    Observable<List<Repo>> listRepos(@Path("user") String user);
}