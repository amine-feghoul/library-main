package com.example.signupscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.signupscreen.Model.Common;
import com.example.signupscreen.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class principal extends AppCompatActivity implements View.OnClickListener {

    EditText email;
    EditText password;
    TextView fpw;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    Button signIn;
    TextView signUp;

    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_principal);


        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        fpw = (TextView) findViewById(R.id.fps);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("users");


        fpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.sendPasswordResetEmail(email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getBaseContext(), "password send to your email", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getBaseContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });


        signIn = findViewById(R.id.login);
        signIn.setOnClickListener(this);
        signUp = findViewById(R.id.signup);
        signUp.setPaintFlags(signUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.login:
                signInWitEmail();
                break;
            case R.id.signup:
                startActivity(new Intent(getApplicationContext(),signup.class));
                break;
        }
    }









    private void signInWitEmail() {
        String Email;
        Email = email.getText().toString();
        if (Email.isEmpty()) {
            email.setError("enter your email");
            email.requestFocus();
            return;
        }

        if (password.getText().toString().isEmpty()) {
            password.setError("enter your password");
            password.requestFocus();
            return;
        }

        progressDialog.setMessage("login in...");
        progressDialog.show();


        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();

                            final FirebaseUser user = firebaseAuth.getCurrentUser();
                            reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Common.currentUser = dataSnapshot.getValue(User.class);
                                    Common.currentUser.setId(user.getUid());
                                    startActivity(new Intent(getApplicationContext(), aceuil.class));
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                                 /* if (firebaseAuth.getCurrentUser().isEmailVerified()){
                                      startActivity(new Intent(getApplicationContext(), aceuil.class));
                                      finish();
                              }
                                  else {
                                      Toast.makeText(getBaseContext(), "please verify ur adress",Toast.LENGTH_LONG).show();
                                  }*/

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
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
