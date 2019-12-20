package com.example.newsapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;

import com.example.newsapp.R;
import com.example.newsapp.adapters.RecyclerViewAdapter;
import com.example.newsapp.api.NewsApiInterface;
import com.example.newsapp.models.Article;
import com.example.newsapp.models.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String BASE_URL = "https://newsapi.org/v2/";
    private static Retrofit retrofit = null;
    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewAdapter adapter;
    private List<Article> articles = new ArrayList<>();

    private final static String API_KEY = "18ae7e49b6fb4db7ba5f05d04922f545";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager (MainActivity.this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        loadData();
    }

    public void loadData(){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }

        NewsApiInterface apiInterface = retrofit.create(NewsApiInterface.class);

        Call <News> call;
        call = apiInterface.getNews("us", API_KEY);
        call.enqueue(new Callback<News>(){
            @Override
            public void onResponse(Call<News> call, Response<News> response){
                articles = response.body().getArticles();
                adapter = new RecyclerViewAdapter(MainActivity.this, articles);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<News> call, Throwable throwable){
                Log.e(TAG, throwable.toString());
            }
        });
    }
}
