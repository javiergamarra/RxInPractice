package com.nhpatt.androidrxinaction;

import com.nhpatt.androidrxinaction.retrofit.GitHubService;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * @author Javier Gamarra
 */
public class RetrofitService {
	public static GitHubService getGithub() {
		Retrofit retrofit = new Retrofit.Builder()
			.baseUrl("https://api.github.com")
			.addConverterFactory(GsonConverterFactory.create())
			.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
			.build();
		return retrofit.create(GitHubService.class);
	}
}
