package com.example.newsapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

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
        loadData("");
    }

    public void loadData(final String keyword){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }

        NewsApiInterface apiInterface = retrofit.create(NewsApiInterface.class);

        Call <News> call;

        //if there's a keyword, fetch data using it, else fetch top headlines
        if ( keyword.length()>2 ){
            call = apiInterface.getNewsWithSearch(keyword, "en", "publishedAt", API_KEY);
        } else {
            Log.e(TAG, "i'm here");
            call = apiInterface.getNews("us", API_KEY);
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2){
                    loadData(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchMenuItem.getIcon().setVisible(false, false);
        return true;
    }
}
