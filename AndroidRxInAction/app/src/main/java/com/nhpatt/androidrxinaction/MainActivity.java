package com.nhpatt.androidrxinaction;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.nhpatt.androidrxinaction.retrofit.GitHubService;
import com.nhpatt.androidrxinaction.retrofit.Repo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

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

		Retrofit retrofit = new Retrofit.Builder()
			.baseUrl("https://api.github.com")
			.addConverterFactory(GsonConverterFactory.create())
			.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
			.build();
		GitHubService service = retrofit.create(GitHubService.class);

		Call<List<Repo>> call = service.listReposSync("nhpatt");
		try {
			Response<List<Repo>> response = call.execute();

			repos.addAll(response.body());
		}
		catch (IOException e) {
			Log.e("TAG", "Error!", e);
		}

	}

	private RepositoryAdapter adapter;
	private List<Repo> repos = new ArrayList<>();
}
