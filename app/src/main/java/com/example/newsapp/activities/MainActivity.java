package com.example.newsapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.example.newsapp.activities.weather;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String BASE_URL = "https://newsapi.org/v2/";
    private static Retrofit retrofit = null;
    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewAdapter adapter;
    private List<Article> articles = new ArrayList<>();
    private Button weatherBtn;
    private Button forexBtn;
    private final static String API_KEY = "18ae7e49b6fb4db7ba5f05d04922f545";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        weatherBtn=findViewById(R.id.button4);
        weatherBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openWeather();
            }
        });
        forexBtn=findViewById(R.id.button5);
        forexBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openforex();
            }
        });


        layoutManager = new LinearLayoutManager (MainActivity.this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        loadData("");


    }
    public void openforex(){
        Intent intent = new Intent(this,Forex.class);
        startActivity(intent);
    }
    public void openWeather(){
        Intent intent = new Intent(this,weather.class);
        startActivity(intent);
    }

    public void loadData(final String keyword){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        NewsApiInterface apiInterface = retrofit.create(NewsApiInterface.class);
        Call <News> call;

        //load shared preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //if there's a keyword, fetch data using it, else fetch top headlines
        if ( keyword.length()>2 ){
            //load language for search results
            String prefLanguage = sharedPreferences.getString("language", "");
            String prefSort = sharedPreferences.getString("sortBy", "publishedAt");

            //check if user is using rigid search and declare call accordingly
            Boolean prefInTitle = sharedPreferences.getBoolean("qInTitle", true);
            call = (prefInTitle)?apiInterface.getNewsWithRigidSearch(keyword, prefLanguage, prefSort, API_KEY):apiInterface.getNewsWithNormalSearch(keyword, prefLanguage, prefSort, API_KEY);
        } else {
            //load country from shared preferences to fetch top headlines
            String prefCountry = sharedPreferences.getString("country", "us");
            call = apiInterface.getNews(prefCountry, API_KEY);
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
        //inflate menu containing search and settings
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        //handle searchView
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

    public void openSettings(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}
