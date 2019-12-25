package com.example.newsapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.newsapp.R;
import com.example.newsapp.activities.ArticleDetailActivity;
import com.example.newsapp.models.Article;

import org.ocpsoft.prettytime.PrettyTime;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

    private Context mContext;
    private List<Article> mData;

    //Request option for Glide
    RequestOptions option;

    //Constructor
    public RecyclerViewAdapter(Context mContext, List<Article> mData) {
        this.mContext = mContext;
        this.mData = mData;
        option = new RequestOptions().centerCrop().placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.news_row_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.view_container.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(mContext, ArticleDetailActivity.class);
                intent.putExtra("title", mData.get(viewHolder.getAdapterPosition()).getTitle());
                intent.putExtra("description", mData.get(viewHolder.getAdapterPosition()).getDescription());
                intent.putExtra("publishedAt", mData.get(viewHolder.getAdapterPosition()).getPublishedAt());
                intent.putExtra("url", mData.get(viewHolder.getAdapterPosition()).getUrl());
                intent.putExtra("imageUrl", mData.get(viewHolder.getAdapterPosition()).getUrlToImage());
                intent.putExtra("content", mData.get(viewHolder.getAdapterPosition()).getContent());
                intent.putExtra("source", mData.get(viewHolder.getAdapterPosition()).getSource().getName());
                mContext.startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(mData.get(position).getTitle());
        holder.published_at.setText("\u2022" + formatDate(mData.get(position).getPublishedAt()));
        holder.source.setText(mData.get(position).getSource().getName());

        //Load image
        Glide.with(mContext).load(mData.get(position).getUrlToImage()).apply(option).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title , published_at, source, time;
        ImageView imageView;
        ConstraintLayout view_container;

        public MyViewHolder (View itemView) {
            super(itemView);

            view_container = itemView.findViewById(R.id.article_row_container);
            title = itemView.findViewById(R.id.title);
            published_at = itemView.findViewById(R.id.published_at);
            source = itemView.findViewById(R.id.source);
            imageView = itemView.findViewById(R.id.thumbnail);

        }

    }
    //helper function to parse json date response into a date object
    public static String formatDate(String string){
        Locale locale = Locale.getDefault();
        String country = String.valueOf(locale.getCountry());
        PrettyTime p = new PrettyTime(new Locale(country.toLowerCase()));
        String isTime = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
                    Locale.ENGLISH);
            Date date = sdf.parse(string);
            isTime = p.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isTime;
    }
}
