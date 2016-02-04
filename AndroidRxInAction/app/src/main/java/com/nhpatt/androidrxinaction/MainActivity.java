package com.nhpatt.androidrxinaction;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.gson.JsonArray;
import com.nhpatt.androidrxinaction.async.GithubAsyncTask;
import com.nhpatt.androidrxinaction.retrofit.Repo;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

	public static final String TAG = "androidrxinaction";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		adapter = new RepositoryAdapter(repos);

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

//		subscription = github.subscribe(newRepos -> {
//			repos.addAll(newRepos);
//			adapter.notifyDataSetChanged();
//		}, throwable -> {
//			Log.e(TAG, throwable.toString());
//		});

		new GithubAsyncTask(this).execute();
	}

	public List<Repo> getRepos() {
		return repos;
	}

	public RepositoryAdapter getAdapter() {
		return adapter;
	}

	@Override
	protected void onStop() {
		super.onStop();

		if (!subscription.isUnsubscribed()) {
			subscription.unsubscribe();
		}
	}

	static Observable<List<Repo>> github = RetrofitService.getGithub()
		.listRepos("nhpatt")
		.doOnCompleted(() -> Log.e(TAG, "Retrieved info"))
		.cache()
		.subscribeOn(Schedulers.io())
		.observeOn(AndroidSchedulers.mainThread());

	private RepositoryAdapter adapter;
	private List<Repo> repos = new ArrayList<>();
	private Subscription subscription;
	private JsonArray _repos;
}
