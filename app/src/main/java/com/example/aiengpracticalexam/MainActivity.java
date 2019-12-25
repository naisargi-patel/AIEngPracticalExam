package com.example.aiengpracticalexam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aiengpracticalexam.helper.EndlessRecyclerViewScrollListener;
import com.example.aiengpracticalexam.helper.Utility;
import com.example.aiengpracticalexam.models.ClsHitResponse;
import com.example.aiengpracticalexam.models.Hit;
import com.example.aiengpracticalexam.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecyclerViewItemClickListener {

    private RecyclerView recyclerView;
    private TextView txtCount;
    private HitListAdapter hitAdapter;
    private int pageNo = 1;
    private ProgressBar progressBar;
    private ArrayList<Hit> hitArrayList = new ArrayList<Hit>();
    private SwipeRefreshLayout swipeRefresh;
    private LinearLayoutManager linearLayoutManager = null;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(getLayoutManager(), MainActivity.this) {
        @Override
        public void onLoadMore(int page, int totalItemsCount) {
            callHitListAPI(true, false);
        }
    };
    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            callHitListAPI(false, true);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initControls();
    }

    private void initControls() {

        txtCount = findViewById(R.id.txtCount);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        setAdapter();

        recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
        swipeRefresh.setOnRefreshListener(refreshListener);

        callHitListAPI(false, false);
    }

    private LinearLayoutManager getLayoutManager() {
        if (linearLayoutManager == null) {
            linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
            return linearLayoutManager;
        } else {
            return linearLayoutManager;
        }
    }

    private void callHitListAPI(Boolean loadMore, Boolean pullToRefresh) {
        if (!loadMore && !pullToRefresh) {
            pageNo = 1;
            hitArrayList.clear();
            progress(true);
        } else if (pullToRefresh) {
            pageNo = 1;
            hitArrayList.clear();
        } else {
            //Load More
            pageNo++;
        }

        if (!Utility.isNetworkAvailable(MainActivity.this)) {
            Toast.makeText(MainActivity.this, "There is issue in the network ", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiClient.getApi().getHitAPI("story", pageNo).enqueue(new Callback<ClsHitResponse>() {

            @Override
            public void onResponse(Call<ClsHitResponse> call, Response<ClsHitResponse> response) {
                progress(false);
                if (response.isSuccessful()) {
                    ClsHitResponse clsHitResponse = response.body();
                    List<Hit> hitList = clsHitResponse.getHits();
                    if (hitList != null) {
                        hitArrayList.addAll(hitList);
                        hitAdapter.notifyDataSetChanged();
                        updateCount();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsHitResponse> call, Throwable t) {
                progress(false);
                if (!call.isCanceled()) {
                    Toast.makeText(MainActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void progress(Boolean start) {
        if (start) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            if (swipeRefresh.isRefreshing()) {
                swipeRefresh.setRefreshing(false);
            }
        }
    }

    private void setAdapter() {
        hitAdapter = new HitListAdapter(this, hitArrayList, MainActivity.this);
        recyclerView.setLayoutManager(getLayoutManager());
        recyclerView.setAdapter(hitAdapter);
    }

    @Override
    public void onItemClick(View view, int pos) {
        switch (view.getId()) {
            case R.id.switchSelection:
                //handle selection
                Hit hit = hitArrayList.get(pos);
                hit.setSelected(!hit.getSelected());
                hitAdapter.notifyItemChanged(pos);
                updateCount();
                break;
        }
    }

    private void updateCount() {
        int count = 0;
        for (Hit hit : hitArrayList) {
            if (hit.getSelected()) {
                count++;
            }
        }
        txtCount.setText("Count : " + count);
    }
}
