package com.example.signupscreen.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signupscreen.Interface.ItemOnClickListener;
import com.example.signupscreen.R;



public class GenreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView t1;
    public ImageView i1;

    public ItemOnClickListener itemOnClickListener;

    public GenreViewHolder(@NonNull View itemView) {
        super(itemView);

        t1 = (TextView)itemView.findViewById(R.id.name);
        i1 = (ImageView)itemView.findViewById(R.id.image);

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
