package com.example.newsapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.newsapp.R;

import org.w3c.dom.Text;

public class ArticleDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView date, time, tv_title, tv_description, tv_author;
    private String mUrl, mImg, mTitle, mAuthor, mDate, mSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);


        //getting extras
        mTitle = getIntent().getExtras().getString("title");
        String author = getIntent().getExtras().getString("author");
        String published_at = getIntent().getExtras().getString("publishedAt");
        String description = getIntent().getExtras().getString("description");
        mUrl = getIntent().getExtras().getString("url");
        String imageUrl = getIntent().getExtras().getString("imageUrl");
        String content = getIntent().getExtras().getString("content");
        mSource = getIntent().getExtras().getString("source");

        //fetching views from layout
        tv_title = findViewById(R.id.title);
        tv_title.setText(mTitle);

        tv_description = findViewById(R.id.description);
        tv_description.setText(description);

        tv_author = findViewById(R.id.author);
        tv_author.setText(author);

        imageView = findViewById(R.id.img);

        //apply image with glide
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.ic_launcher_foreground);
        Glide.with(this).load(imageUrl).apply(requestOptions).into(imageView);

        time = findViewById(R.id.time);


    }

    //handle click that will open the article in the browser
    public void openArticleUrl(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(this.mUrl)));
    }

    //handle click for share
    public void shareArticle(View view){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, mSource);
        String body = mTitle + "\n" + mUrl + "\n" + "Shared from the News App" + "\n";
        intent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(intent);
    }
}
