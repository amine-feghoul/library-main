package com.example.signupscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.signupscreen.Interface.ItemOnClickListener;
import com.example.signupscreen.Model.BooksItem;
import com.example.signupscreen.Model.Common;
import com.example.signupscreen.ViewHolder.BooksViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class search extends AppCompatActivity implements TextWatcher {

    RecyclerView recyclerView;
    TextView search_text,text1,text2,text3;
    ImageView search_icon;
    EditText bookName;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference booksRefrence,rateReference;
    FirebaseRecyclerAdapter<BooksItem, BooksViewHolder> bookAdapter;

    FirebaseRecyclerOptions<BooksItem>bookoptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        bookName = (EditText)findViewById(R.id.search);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        search_icon = findViewById(R.id.icon_search);

        bookName.addTextChangedListener(this);


        firebaseDatabase = FirebaseDatabase.getInstance();
        booksRefrence = firebaseDatabase.getReference("Books");
        rateReference = firebaseDatabase.getReference("Rating");

        recyclerView = findViewById(R.id.searchrv);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        bookName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (bookName.getText().toString().length() > 0) {
                        loadSearchData();
                        return true;

                    }
                }
                return false;
            }
        });
    }




    private void loadSearchData() {

       /* bookName.clearFocus();
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(bookName.getWindowToken(),0);*/

        text1.setVisibility(View.GONE);
        text2.setVisibility(View.GONE);
        text3.setVisibility(View.GONE);
        search_icon.setVisibility(View.GONE);

        Query q = booksRefrence.orderByChild("name").startAt(bookName.getText().toString().trim())
                .endAt(bookName.getText().toString().trim()+"\uf8ff");
        bookoptions = new FirebaseRecyclerOptions.Builder<BooksItem>().setQuery(q,BooksItem.class).build();
         bookAdapter = new FirebaseRecyclerAdapter<BooksItem,BooksViewHolder>(bookoptions) {
            @Override
            protected void onBindViewHolder(@NonNull BooksViewHolder holder, int position, @NonNull final BooksItem model) {
                holder.bookname.setText(model.getName());
                holder.bookpage.setText(model.getPage()+" pages");
                Picasso.get().load(model.getImage()).into(holder.bookimage);
                if(model.rate!=null ){
                    holder.bookRate.setVisibility(View.VISIBLE);
                    holder.bookRate.setText(""+model.getRate());
                }
                final BooksItem item = model;
                holder.setItemOnClickListener(new ItemOnClickListener() {

                    @Override
                    public void OnClick(View v, int position, boolean isLong) {
                        Common.currentBook = item;
                        Common.currentBook.setId(bookAdapter.getRef(position).getKey());
                        startActivity(new Intent(getApplicationContext(),BookDetail.class));
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



        bookAdapter.startListening();
        recyclerView.setAdapter(bookAdapter);

    }


    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }


    public void onTextChanged(CharSequence s, int start, int before, int count) {
        loadSearchData();
    }



    public void afterTextChanged(Editable s) {

    }
}
