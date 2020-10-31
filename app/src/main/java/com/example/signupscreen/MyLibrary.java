package com.example.signupscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.signupscreen.Model.BooksItem;
import com.example.signupscreen.Model.Common;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyLibrary extends AppCompatActivity {
    BottomNavigationView navigationView;
    RecyclerView myLibraryRecycler;
    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<BooksItem> favoriteBooks;
    PopularBookAdapter adapter;

    private long backPressedTime;
    private Toast backToast;

    ImageView home,library,profil,emptylib;
    TextView tv,nobook,nobooktwo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_library);

        nobook = findViewById(R.id.nobook);
        nobooktwo = findViewById(R.id.nobooktwo);
       emptylib = findViewById(R.id.emptylib);

        home = (ImageView)findViewById(R.id.home);
        library = (ImageView)findViewById(R.id.library);
        profil = (ImageView)findViewById(R.id.profil);

        tv = findViewById(R.id.tv);



        database = FirebaseDatabase.getInstance();
        reference=database.getReference("Books");


        myLibraryRecycler = findViewById(R.id.myLibraryRecyclerView);
        myLibraryRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        // loadMylibraryPaths();
        LoadData();
        showMyLibrary();





        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getApplicationContext(),aceuil.class));
            }
        });
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getApplicationContext(),Profile.class));
            }
        });


    }



    private void showMyLibrary() {

        myLibraryRecycler.setAdapter(adapter);
    }

    private void LoadData() {

        favoriteBooks = new ArrayList<>();
        adapter = new PopularBookAdapter(this,favoriteBooks);
        favoriteBooks.clear();

        for(final String s:Common.favoriteBooks){
            reference.child(s).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    nobook.setVisibility(View.GONE);
                    nobooktwo.setVisibility(View.GONE);
                    emptylib.setVisibility(View.GONE);
                    tv.setVisibility(View.VISIBLE);
                    BooksItem tmp = dataSnapshot.getValue(BooksItem.class);
                    tmp.setId(s);
                    favoriteBooks.add(tmp);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        //adapter.notifyDataSetChanged();

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