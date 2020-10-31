package com.example.signupscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.signupscreen.ViewHolder.BooksViewHolder;
import com.example.signupscreen.Interface.ItemOnClickListener;
import com.example.signupscreen.Model.BooksItem;
import com.example.signupscreen.Model.Common;
import com.example.signupscreen.Model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Path;
import com.squareup.picasso.Picasso;

public class BooksList extends AppCompatActivity {
    String category;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference,rateReference,refrnce;
    RecyclerView recyclerView;
    TextView genrename;

    FirebaseRecyclerOptions<BooksItem> options;
    FirebaseRecyclerAdapter<BooksItem, BooksViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_list);
        if(getIntent()!=null){
            category = getIntent().getStringExtra("category");
            Toast.makeText(getApplicationContext(),category,Toast.LENGTH_SHORT).show();
        }
        firebaseDatabase = FirebaseDatabase.getInstance();

        reference = firebaseDatabase.getReference("Books");
        rateReference = firebaseDatabase.getReference("Rating");

        refrnce =  firebaseDatabase.getReference().child("Genre").child(category);

        recyclerView = findViewById(R.id.booksListRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        genrename = findViewById(R.id.genre_name);
        genrename.setText(category);

        Toast.makeText(getApplicationContext(),""+Common.popularBooks.size(),Toast.LENGTH_LONG).show();


        loadData();

        refrnce.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                genrename.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void loadData() {
        Query q = reference.orderByChild("category").equalTo(category);


        options = new FirebaseRecyclerOptions.Builder<BooksItem>().setQuery(q,BooksItem.class).build();

        adapter = new FirebaseRecyclerAdapter<BooksItem, BooksViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final BooksViewHolder holder, final int position, @NonNull BooksItem model) {
                holder.bookname.setText(model.getName());
                holder.bookpage.setText(model.getPage());
                holder.bookauthor.setText("by "+model.getAuthorname());
                Picasso.get().load(model.getImage()).into(holder.bookimage);
                if(model.rate!=null ){
                    holder.bookRate.setVisibility(View.VISIBLE);
                    holder.bookRate.setText(""+model.getRate());
                }
                final BooksItem itemClick = model;

                holder.setItemOnClickListener(new ItemOnClickListener() {
                    @Override
                    public void OnClick(View v, int position, boolean isLong) {
                        Common.currentBook = itemClick;
                        Common.currentBook.setId(adapter.getRef(position).getKey());
                        Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),BookDetail.class));
                        Toast.makeText(getApplicationContext(),adapter.getRef(position).getKey(),Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public BooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.try_again,parent,false);
                return new BooksViewHolder(v);
            }


        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}
