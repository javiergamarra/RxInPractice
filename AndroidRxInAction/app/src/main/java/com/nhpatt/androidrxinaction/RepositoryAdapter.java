package com.nhpatt.androidrxinaction;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhpatt.androidrxinaction.retrofit.Repo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.ReposViewHolder> {
	public RepositoryAdapter(List<Repo> repos) {
		this.repos = repos;
	}

	@Override
	public ReposViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
		return new ReposViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ReposViewHolder holder, int position) {
		holder.bind(repos.get(position));
	}

	@Override
	public int getItemCount() {
		return repos.size();
	}

	private final List<Repo> repos;

	public class ReposViewHolder extends RecyclerView.ViewHolder {

		public ReposViewHolder(View itemView) {
			super(itemView);

			name = (TextView) itemView.findViewById(R.id.item_name);
			date = (TextView) itemView.findViewById(R.id.item_date);
		}

		public void bind(Repo repo) {
			name.setText(repo.getName());
			date.setText(new SimpleDateFormat("dd/MM").format(new Date()));
		}

		private final TextView name;
		private final TextView date;
	}
}