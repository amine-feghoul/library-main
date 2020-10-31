package com.example.signupscreen.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signupscreen.Model.BooksItem;
import com.example.signupscreen.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.BookViewHolder> {
    Context context;
    ArrayList<BooksItem> bookArrayList;
    public PopularAdapter(@NonNull View itemView) {

    }

    public PopularAdapter(Context context,ArrayList<BooksItem> arrayList) {
        this.context = context;
        this.bookArrayList = arrayList;
    }

    @NonNull
    @Override
    public PopularAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.popular_books,parent,false);
        return new BookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.BookViewHolder holder, int position) {
        BooksItem book = bookArrayList.get(position);
        holder.bookName.setText(book.getName());
        holder.bookAuthor.setText(book.getAuthorname());
        Picasso.get().load(book.getImage()).into(holder.bookImage);
    }

    @Override
    public int getItemCount() {
        return bookArrayList.size();
    }
    class BookViewHolder extends RecyclerView.ViewHolder {
        TextView bookName,bookAuthor;
        ImageView bookImage;
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.book_name);
            bookImage = itemView.findViewById(R.id.book_image);
            bookAuthor = itemView.findViewById(R.id.book_author);
        }
    }


}
