package com.example.signupscreen;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.load.engine.Resource;
import com.example.signupscreen.Interface.ItemOnClickListener;
import com.example.signupscreen.Model.BooksItem;
import com.example.signupscreen.Model.Common;
import com.example.signupscreen.Model.GenreItem;

import com.example.signupscreen.ViewHolder.PopularAdapter;
import com.example.signupscreen.ViewHolder.BooksViewHolder;
import com.example.signupscreen.ViewHolder.GenreViewHolder;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class aceuil extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;

    ViewFlipper viewFlipper;
    RecyclerView recyclerView2,popularRecyclerView,recenetRecycler;
    ImageView search;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference categoriesReference,booksRefrence,ratedBooksReference,userRefrence;
    FirebaseRecyclerAdapter<BooksItem, BooksViewHolder> bookAdapter;

    FirebaseRecyclerOptions<BooksItem>bookoptions;
    DatabaseReference reference;
    FirebaseRecyclerAdapter<GenreItem, GenreViewHolder> adapter;
    FirebaseRecyclerOptions<GenreItem> options;

    FirebaseRecyclerOptions<BooksItem>recentoptions;
    FirebaseRecyclerAdapter<BooksItem, BooksViewHolder> recentAdapter;


    ArrayList<String> paths;
    ArrayList<DatabaseReference> popularBooksRefrences;
    ArrayList<BooksItem> popularbooks;
    PopularBookAdapter adapterPopular;
    PopularAdapter adapterpop;

    ImageView home,library,profil;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aceuil);


        home = (ImageView)findViewById(R.id.home);
        library = (ImageView)findViewById(R.id.library);
        profil = (ImageView)findViewById(R.id.profil);




        viewFlipper = (ViewFlipper)findViewById(R.id.viewflipper);

        int images [] = {R.drawable.imgone,R.drawable.imgtwo,R.drawable.imgfive,R.drawable.imgthree,R.drawable.imgfour};

        for(int i = 0; i<images.length; i++){
            fliperimage(images[i]);
        }

        popularbooks = new ArrayList<BooksItem>();
        popularBooksRefrences = new ArrayList<DatabaseReference>();

        paths =  new ArrayList<String>();


        recyclerView2 = findViewById(R.id.rvgenre);
        recyclerView2.setHasFixedSize(true);

        popularRecyclerView = findViewById(R.id.popularbooksrv);
        recyclerView2.setHasFixedSize(true);

        recenetRecycler = findViewById(R.id.recentbookrv);
        LinearLayoutManager tmp = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        tmp.setReverseLayout(true);
        tmp.setStackFromEnd(true);
        recenetRecycler.setLayoutManager(tmp);


        firebaseDatabase = FirebaseDatabase.getInstance();

        userRefrence = firebaseDatabase.getReference("users").child(Common.currentUser.getId());
        ratedBooksReference = firebaseDatabase.getReference("Rating");

        categoriesReference = firebaseDatabase.getReference("Genre");
        booksRefrence = firebaseDatabase.getReference("Books");

        search = (ImageView) findViewById(R.id.search);





        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getApplicationContext(),2);
        gridLayoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView2.setLayoutManager(gridLayoutManager2);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        popularRecyclerView.setLayoutManager(gridLayoutManager);

        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getApplicationContext(),MyLibrary.class));
            }
        });
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getApplicationContext(),Profile.class));
            }
        });







        adapterpop = new PopularAdapter(getApplicationContext(),popularbooks);
        popularRecyclerView.setAdapter(adapterpop);



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),search.class);
                startActivity(i);
            }
        });



    }
    @Override
    protected void onResume() {
        super.onResume();

        loadGenre(categoriesReference);
        LoadRatedBooks();
        showPopularBooks();
        loadMylibraryPaths();
        // bookName.setActivated(false);
    }

    private void showPopularBooks() {
        adapterPopular = new PopularBookAdapter(this,Common.popularBooks);
        adapterPopular.notifyItemInserted(Common.popularBooks.size());
        adapterPopular.notifyDataSetChanged();
        popularRecyclerView.setAdapter(adapterPopular);

        // popularRecyclerView;
        // popularRecyclerView.smoothScrollToPosition(Common.popularBooks.size()-1);

    }


    private void LoadRatedBooks() {
        Common.popularBooks = new ArrayList<BooksItem>();
        adapterPopular = new PopularBookAdapter(this,Common.popularBooks);
        Query q = ratedBooksReference.orderByChild("rate").limitToLast(30);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                paths.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    paths.add(String.valueOf(snapshot.getKey()));
                }
                final ArrayList<String> temp = new ArrayList<String>();

                int i = paths.size()-1;
                Toast.makeText(getApplicationContext(), ""+i, Toast.LENGTH_LONG).show();
                for( int j=i ;j>=0;j--){
                    temp.add(paths.get(j));
                }
                paths = temp;
                Common.popularBooks.clear();
                for(String s:paths){
                    booksRefrence.child(s).addValueEventListener(new ValueEventListener() {

                                           @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                          BooksItem tmp = dataSnapshot.getValue(BooksItem.class);
                                        tmp.setId(dataSnapshot.getKey());
                                          Common.popularBooks.add(tmp);
                                           adapterPopular.notifyDataSetChanged();
                                           popularbooks.add(dataSnapshot.getValue(BooksItem.class));
                                                                     }

                                              @Override
                                             public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                     }
                                                                 }

                    );


                    Toast.makeText(getApplicationContext(),""+ Common.popularBooks.size(),Toast.LENGTH_SHORT).show();




                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }





    private void loadGenre(Query q) {
        options = new FirebaseRecyclerOptions.Builder<GenreItem>().setQuery(q,GenreItem.class).build();
        adapter = new FirebaseRecyclerAdapter<GenreItem, GenreViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull GenreViewHolder holder, int position, @NonNull GenreItem model) {
                holder.t1.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.i1);
                holder.setItemOnClickListener(new ItemOnClickListener() {
                    @Override
                    public void OnClick(View v, int position, boolean isLong) {
                        startActivity(new Intent(getApplicationContext(),BooksList.class).putExtra("category",adapter.getRef(position).getKey()));
                    }
                });
            }

            @NonNull
            @Override
            public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_genre_item,parent,false);
                return new GenreViewHolder(v);
            }

        };
        adapter.startListening();
        recyclerView2.setAdapter(adapter);

        Query recent = booksRefrence.orderByChild("year").limitToLast(30);
        recentoptions = new FirebaseRecyclerOptions.Builder<BooksItem>().setQuery(recent,BooksItem.class).build();
        recentAdapter =  new FirebaseRecyclerAdapter<BooksItem, BooksViewHolder>(recentoptions) {
            @Override
         protected void onBindViewHolder(@NonNull BooksViewHolder holder, int position, @NonNull final BooksItem model) {
                holder.bookname.setText(model.getName());
                holder.bookauthor.setText(model.getAuthorname());
                Picasso.get().load(model.getImage()).into(holder.bookimage);
                final BooksItem item = model;
                holder.setItemOnClickListener(new ItemOnClickListener() {

                    @Override
                    public void OnClick(View v, int position, boolean isLong) {
                        Common.currentBook = item;
                        Common.currentBook.setId(recentAdapter.getRef(position).getKey());
                        startActivity(new Intent(getApplicationContext(), BookDetail.class));
                    }

                });
            }

            @NonNull
            @Override
            public BooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_books, parent, false);
                return new BooksViewHolder(v);
            }
        };
        recentAdapter.startListening();

        recenetRecycler.setAdapter(recentAdapter);

    }

    public void fliperimage(int image){

        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setAutoStart(true);

        viewFlipper.setInAnimation(this,android.R.anim.slide_out_right);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_in_left);
    }

    private void loadMylibraryPaths() {
        Common.favoriteBooks = new ArrayList<String>();
        userRefrence.child("favorite").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Common.favoriteBooks.add(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            System.exit(0);
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
