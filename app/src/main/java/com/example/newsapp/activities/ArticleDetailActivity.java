package com.example.newsapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.newsapp.R;

import org.w3c.dom.Text;

public class ArticleDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView date, time, tv_title, tv_content;
    private String mUrl, mImg, mTitle, mAuthor, mDate, mSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);


        //getting extras
        String title = getIntent().getExtras().getString("title");
        String author = getIntent().getExtras().getString("author");
        String published_at = getIntent().getExtras().getString("publishedAt");
        String description = getIntent().getExtras().getString("description");
        String url = getIntent().getExtras().getString("url");
        String imageUrl = getIntent().getExtras().getString("imageUrl");
        String content = getIntent().getExtras().getString("content");

        //
        tv_title = findViewById(R.id.title);
        tv_title.setText(title);

        tv_content = findViewById(R.id.content);
        tv_content.setText(content);

        imageView = findViewById(R.id.img);

        //apply image with glide
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.ic_launcher_foreground);
        Glide.with(this).load(imageUrl).apply(requestOptions).into(imageView);

        time = findViewById(R.id.time);


    }
}
