package com.nhpatt.androidrxinaction;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.nhpatt.androidrxinaction.retrofit.Repo;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

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

		github.subscribe(newRepos -> {
			repos.addAll(newRepos);
			adapter.notifyDataSetChanged();
		});
	}

	static Observable<List<Repo>> github = RetrofitService.getGithub().listRepos("nhpatt")
		.subscribeOn(Schedulers.io())
		.observeOn(AndroidSchedulers.mainThread()).cache();

	private RepositoryAdapter adapter;
	private List<Repo> repos = new ArrayList<>();
}
