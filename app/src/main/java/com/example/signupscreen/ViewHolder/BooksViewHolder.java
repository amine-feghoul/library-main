package com.example.signupscreen.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signupscreen.Interface.ItemOnClickListener;
import com.example.signupscreen.R;



public class BooksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView bookname,bookauthor,bookRate,bookpage;
    public ImageView bookimage;

    public ItemOnClickListener itemOnClickListener;

    public BooksViewHolder(@NonNull View itemView) {
        super(itemView);

        bookname = (TextView)itemView.findViewById(R.id.book_name);
        bookauthor = (TextView)itemView.findViewById(R.id.book_author);
        bookimage = (ImageView)itemView.findViewById(R.id.book_image);
        bookRate = itemView.findViewById(R.id.rating);
        bookpage =  itemView.findViewById(R.id.book_pages);
        //bookRate.setVisibility(View.GONE);

        itemView.setOnClickListener(this);

    }
    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener){
        this.itemOnClickListener = itemOnClickListener;
    }
    @Override
    public void onClick(View v) {
        itemOnClickListener.OnClick(v,getAdapterPosition(),false);
    }
}