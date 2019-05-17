package com.example.newsarena;


import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.newsarena.Adapter.ListSourceAdapter;
import com.example.newsarena.Common.Common;
import com.example.newsarena.Interface.NewsService;
import com.example.newsarena.Model.WebSite;
import com.google.gson.Gson;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView listWebSite;
    RecyclerView.LayoutManager layoutManager;
    NewsService mService;
    ListSourceAdapter adapter;
    AlertDialog dialog;
    SwipeRefreshLayout swipelayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init cache
        Paper.init(this);

        //Init Service
        mService = Common.getNewsService();


        //Init View
        swipelayout = findViewById(R.id.swipeRefresh);
        swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWebsiteSource(true);
            }
        });
        listWebSite = findViewById(R.id.list_source);
        listWebSite.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listWebSite.setLayoutManager(layoutManager);

        dialog = new SpotsDialog.Builder().setContext(this).build();
        loadWebsiteSource(false);
    }

    private void loadWebsiteSource(boolean isRefreshed) {
        if(!isRefreshed)
        {
            String cache = Paper.book().read("cache");
            if(cache!=null && !cache.isEmpty()&& !cache.equals("null")){
                WebSite website = new Gson().fromJson(cache,WebSite.class);
                adapter = new ListSourceAdapter(getBaseContext(),website);
                adapter.notifyDataSetChanged();
                listWebSite.setAdapter(adapter);
            }
            else
            {

                dialog.show();
                mService.getSources().enqueue(new Callback<WebSite>() {
                    @Override
                    public void onResponse(Call<WebSite> call, Response<WebSite> response) {
                        adapter = new ListSourceAdapter(getBaseContext(),response.body());
                        adapter.notifyDataSetChanged();
                        listWebSite.setAdapter(adapter);

                        Paper.book().write("cache",new Gson().toJson(response.body()));
                        swipelayout.setRefreshing(false);
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<WebSite> call, Throwable t) {

                    }
                });
            }
        }
        else //if from swipe to refresh
        {
            swipelayout.setRefreshing(true);
            dialog.show();
            mService.getSources().enqueue(new Callback<WebSite>() {
                @Override
                public void onResponse(Call<WebSite> call, Response<WebSite> response) {
                    adapter = new ListSourceAdapter(getBaseContext(),response.body());
                    adapter.notifyDataSetChanged();
                    listWebSite.setAdapter(adapter);

                    Paper.book().write("cache",new Gson().toJson(response.body()));
                    swipelayout.setRefreshing(false);
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<WebSite> call, Throwable t) {

                }
            });
        }
    }
}
