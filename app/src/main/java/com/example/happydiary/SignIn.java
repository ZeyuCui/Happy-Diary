package com.example.happydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.happydiary.Zeyu.ListActivity;
import com.example.happydiaryy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class SignIn extends AppCompatActivity {

    // shared instance of auth obj
    private FirebaseAuth mAuth;

    private EditText emailInput;
    private EditText pwInput;
    private Button signInBtn;
    private Button createAccountBtn;
    private TextView forgotPW;


    ConstraintLayout mView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();


        mView=findViewById(R.id.mView);

        // set up elements
        emailInput = (EditText) findViewById(R.id.input_email);
        pwInput = (EditText) findViewById(R.id.input_password);
        signInBtn = (Button) findViewById(R.id.button_signIn);
        createAccountBtn = (Button) findViewById(R.id.button_createAccountPage);
        forgotPW = (TextView) findViewById(R.id.link_forgotPW);

        // click listeners
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get email / password
                String email = emailInput.getText().toString();
                String password = pwInput.getText().toString();

                if(email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignIn.this, "Enter your email and password", Toast.LENGTH_LONG).show();
                }
                else {
                    signIn(email, password);
                }



            }
        });
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // direct to create account activity
                Intent intent = new Intent(SignIn.this, CreateAccount.class);
                startActivity(intent);
            }
        });
        forgotPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this, ForgotPassword.class);
                startActivity(intent);
            }
        });



    }

    @Override
    public void onStart() {

        super.onStart();
        FirebaseUser current = mAuth.getCurrentUser();
        if(current != null) {
            current.reload();
        }
    }

    protected void signIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            if(user.isEmailVerified()) {
                                Intent intent = new Intent(SignIn.this, CalendarMainActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(SignIn.this, "Please verify email", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                            }

                        }
                        else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                });

    }


    @Override
    protected void onResume() {
        super.onResume();
        mView.setBackgroundColor(getColor(SP.getInstance(this).getInt("theme", R.color.white)));
    }

}