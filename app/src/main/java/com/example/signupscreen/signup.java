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

public class signup extends AppCompatActivity implements View.OnClickListener {
    EditText email;
    EditText password;
    EditText name;
    Button signup;
    TextView login;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);


        email = (EditText)findViewById(R.id.email);
        name = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("users");





        signup = findViewById(R.id.signup);
        signup.setOnClickListener(this);
        login =  findViewById(R.id.login);
        login.setPaintFlags(login.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.signup:
                signUpWitEmail();
                break;
            case R.id.login:
                startActivity(new Intent(getApplicationContext(),principal.class));
                break;
        }
    }

    private void signUpWitEmail() {

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
        if (name.getText().toString().isEmpty()) {
            name.setError("enter your username");
            name.requestFocus();
            return;

        }

        progressDialog.setMessage("signin up...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getBaseContext(), "signup successfully pliz verify your adress", Toast.LENGTH_LONG).show();
                                        email.setText("");
                                        password.setText("");
                                        name.setText("");
                                    }

                                }
                            });




                            Intent intent = new Intent(signup.this, principal.class);
                            startActivity(intent);
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                    User user = new User(password.getText().toString(), name.getText().toString());
                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                            //startActivity(new Intent(signup.this,home.class));
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), task.getException().getMessage(),Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

}
