package com.example.newsarena.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.newsarena.Common.ISO8601Parse;
import com.example.newsarena.DetailArticle;
import com.example.newsarena.Interface.ItemClickListener;
import com.example.newsarena.Model.Article;
import com.example.newsarena.R;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

class ListNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ItemClickListener itemClickListener;
    TextView article_title;
    RelativeTimeTextView article_time;
    CircleImageView article_image;

    public ListNewsViewHolder(@NonNull View itemView) {
        super(itemView);

        article_image = itemView.findViewById(R.id.article_image);
        article_time = itemView.findViewById(R.id.article_time);
        article_title = itemView.findViewById(R.id.article_title);
        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}


public class ListNewsAdapter extends RecyclerView.Adapter<ListNewsViewHolder> {

    private List<Article> articleList;
    private Context context;

    public ListNewsAdapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
    }

    @NonNull
    @Override
    public ListNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.news_layout, parent, false);
        return new ListNewsViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ListNewsViewHolder holder, int position) {
        Picasso.get()
                .load(articleList.get(position).getUrlToImage())
                .into(holder.article_image);

        if (articleList.get(position).getTitle().length() > 65)
            holder.article_title.setText(articleList.get(position).getTitle().substring(0, 65) + "...");
        else
            holder.article_title.setText(articleList.get(position).getTitle());
    if(articleList.get(position).getPublishedAt() != null) {
        Date date = null;
        try {
            date = ISO8601Parse.parse(articleList.get(position).getPublishedAt());
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        holder.article_time.setReferenceTime(date.getTime());
    }

        //event click
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                Intent detail = new Intent(context,DetailArticle.class);
                detail.putExtra("webURL",articleList.get(position).getUrl());
                detail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(detail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
}
