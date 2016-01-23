package com.nhpatt.androidrxinaction.async;

import android.os.AsyncTask;
import android.util.Log;

import com.nhpatt.androidrxinaction.MainActivity;
import com.nhpatt.androidrxinaction.RetrofitService;
import com.nhpatt.androidrxinaction.retrofit.Repo;

import java.io.IOException;
import java.util.List;

/**
 * @author Javier Gamarra
 */
public class GithubAsyncTask extends AsyncTask<Void, Void, List<Repo>> {

	public GithubAsyncTask(MainActivity activity) {
		this.activity = activity;
	}

	@Override
	protected List<Repo> doInBackground(Void... params) {
		try {
			return RetrofitService.getGithub().oldListRepos("nhpatt").execute().body();
		}
		catch (IOException e) {
			Log.e(MainActivity.TAG, "Error retrieving info");
		}
		return null;
	}

	@Override
	protected void onPostExecute(List<Repo> repos) {
		super.onPostExecute(repos);

		activity.getRepos().addAll(repos);
		activity.getAdapter().notifyDataSetChanged();
	}

	private final MainActivity activity;
}
