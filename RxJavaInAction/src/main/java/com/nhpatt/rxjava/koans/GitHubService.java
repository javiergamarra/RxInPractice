package com.nhpatt.rxjava.koans;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

import java.util.List;

public interface GitHubService {

    @GET("/users/{user}/repos")
    @Headers("User-Agent: nhpatt")
    Observable<List<Repo>> listRepos(@Path("user") String user);

    @GET("/repos/{user}/{repo}/commits")
    @Headers("User-Agent: nhpatt")
    Observable<List<Commit>> listCommits(
            @Path("user") String user, @Path("repo") String repo);
}