package com.example.signupscreen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.signupscreen.Model.Common;
import com.example.signupscreen.Model.Rating;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;



import java.util.List;



public class BookDetail extends AppCompatActivity implements View.OnClickListener, RatingDialogListener {
    TextView bookName,auther,discription;
    ImageView bookCover,ratingBtn,addFavorite,bgcover;

    ConstraintLayout constraintLayout;
    RatingBar ratingBar;

    private int BLUR_PRECENTAGE = 50;

    Button read;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference refrence,user,bookrefrence;
    Float rate;
    List<String> paths;

    boolean isFavorite ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        bookCover = findViewById(R.id.book_image);
        bgcover = findViewById(R.id.bg_cover);
        bookName = findViewById(R.id.book_name);
        discription = findViewById(R.id.description);
        auther = findViewById( R.id.book_author);
        read = findViewById( R.id.btnread);
        ratingBar = findViewById(R.id.ratingBar);
        ratingBtn = findViewById(R.id.ratingBtn);
        read.setOnClickListener(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        refrence = firebaseDatabase.getReference("Rating");
        user = firebaseDatabase.getReference("users").child(Common.currentUser.getId()).child("favorite");
        bookrefrence = firebaseDatabase.getReference("Books").child(Common.currentBook.getId()).child("rate");

        ratingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });

        bookName.setText(Common.currentBook.getName());
        auther.setText(Common.currentBook.getAuthorname());
        discription.setText(Common.currentBook.getDescription());
        Picasso.get().load(Common.currentBook.getImage()).into(bookCover);
        Picasso.get().load(Common.currentBook.getImage()).into(bgcover);



        addFavorite=findViewById(R.id.addFavorite);
        addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrRemove();
            }
        });
        isFavorite();
        loadRate();

    }



    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("submit")
                .setNegativeButtonText("cancel")
                .setDefaultRating(0)
                .setTitle("rate this book")
                .setNumberOfStars(5)
                .setCommentInputEnabled(false)
                .create(BookDetail.this).show();
    }


    private void loadRate(){
        refrence.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(Common.currentBook.getId()).exists()){
                    rate =  dataSnapshot.child(Common.currentBook.getId()).child("rate").getValue(Float.class);
                    //Log.d("rate",rate);
                    //Toast.makeText(getApplicationContext(),rate,Toast.LENGTH_SHORT).show();
                    ratingBar.setRating(rate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnread:
                Log.d("pdfLink",""+Common.currentBook.getPdf());
                startActivity(new Intent(getApplicationContext(),PdfReader.class));
                break;
            case R.id.addFavorite:
                addOrRemove();
                break;
        }

    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int i, @NotNull String s) {
        final Rating rate = new Rating(i,s);
        if(i!=0)
            refrence.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.child(Common.currentBook.getId()).exists()){
            if(dataSnapshot.child(Common.currentBook.getId()).child("users").child(Common.currentUser.getId()).exists()){
             int prating = dataSnapshot.child(Common.currentBook.getId()).child("users").child(Common.currentUser.getId())
                .child("rating").getValue(Integer.class);
                            //   Log.d("prating" , prating);
                            int totalRates = dataSnapshot.child(Common.currentBook.getId()).child("totalRates").getValue(Integer.class);
                            refrence.child(Common.currentBook.getId()).child("users").child(Common.currentUser.getId()).setValue(rate);
                            int total = totalRates+rate.getRating()-prating;
                            Log.d("total",""+total);
                            refrence.child(Common.currentBook.getId()).child("totalRates").setValue(total );
                            long numberOfRates = dataSnapshot.child(Common.currentBook.getId()).child("users").getChildrenCount();
                            refrence.child(Common.currentBook.getId()).child("rate").setValue((float)total/numberOfRates );
                            bookrefrence.setValue((float)total/numberOfRates);
                        }
                        else{
                            int totalRates = dataSnapshot.child(Common.currentBook.getId()).child("totalRates").getValue(Integer.class);
                            refrence.child(Common.currentBook.getId()).child("users").child(Common.currentUser.getId()).setValue(rate);
                            int total = totalRates + rate.getRating();
                            refrence.child(Common.currentBook.getId()).child("totalRates").setValue(total );

                            long numberOfRates = dataSnapshot.child(Common.currentBook.getId()).child("users").getChildrenCount();
                            refrence.child(Common.currentBook.getId()).child("rate").setValue((float)(total/(numberOfRates +1)));
                            bookrefrence.setValue((float)total/numberOfRates);
                        }
                    }
                    else{
                        refrence.child(Common.currentBook.getId()).child("totalRates").setValue(rate.getRating());
                        refrence.child(Common.currentBook.getId()).child("users").child(Common.currentUser.getId()).setValue(rate);
                        refrence.child(Common.currentBook.getId()).child("rate").setValue(rate.getRating() );
                        bookrefrence.setValue(rate.getRating());
                    }
                    loadRate();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {


                }
            });
    }

    private void isFavorite(){
        if(Common.favoriteBooks.contains(Common.currentBook.getId())){
            isFavorite=true;
            addFavorite.setImageResource(R.drawable.ic_favorite_full_grey);
        }
        else{
            isFavorite=false;
            addFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }


    private void addOrRemove(){
        if(isFavorite){
            isFavorite = false;
            user.child(Common.currentBook.getId()).removeValue();
            Common.favoriteBooks.remove(Common.favoriteBooks.indexOf(Common.currentBook.getId()));
            isFavorite();
        }else{
            user.child(Common.currentBook.getId()).setValue(Common.currentBook.getName());
            isFavorite = true;
            //addFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
            if(!Common.favoriteBooks.contains(Common.currentBook.getId()))
                Common.favoriteBooks.add(Common.currentBook.getId());
            isFavorite();


        }}

    @Override
    public void onNeutralButtonClicked() {

    }
}