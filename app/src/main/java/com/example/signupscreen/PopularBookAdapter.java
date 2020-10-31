package com.example.signupscreen;

import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signupscreen.ViewHolder.BooksViewHolder;
import com.example.signupscreen.Interface.ItemOnClickListener;
import com.example.signupscreen.Model.BooksItem;
import com.example.signupscreen.Model.Common;
import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class PopularBookAdapter extends RecyclerView.Adapter<BooksViewHolder> {
    public PopularBookAdapter() {
    }
    ArrayList<BooksItem> books;
    Context context ;

    public PopularBookAdapter(Context context,ArrayList<BooksItem> books) {
        this.books = books;
        this.context = context;
    }

    @NonNull
    @Override
    public BooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_books,parent,false);
        BooksViewHolder holder = new BooksViewHolder(v);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull BooksViewHolder holder, int position) {
        holder.bookname.setText(books.get(position).getName());
        holder.bookauthor.setText(books.get(position).getAuthorname());

        Picasso.get().load(books.get(position).getImage()).into(holder.bookimage);
        holder.setItemOnClickListener(new ItemOnClickListener() {
            @Override
            public void OnClick(View v, int position, boolean isLong) {
                Common.currentBook = books.get(position);
                Intent i = new Intent(context,BookDetail.class);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}

