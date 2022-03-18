package com.example.happydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class SignIn extends AppCompatActivity {

    // shared instance of auth obj
    private FirebaseAuth mAuth;

    private EditText emailInput;
    private EditText pwInput;
    private Button signInBtn;
    private Button createAccountBtn;
    private TextView forgotPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        // set up elements
        emailInput = (EditText) findViewById(R.id.input_email);
        pwInput = (EditText) findViewById(R.id.input_password);
        signInBtn = (Button) findViewById(R.id.button_signIn);
        createAccountBtn = (Button) findViewById(R.id.button_createAccountPage);
        forgotPW = (TextView) findViewById(R.id.link_forgotPW);

       /* reference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userRecord=snapshot.getValue().toString();
                System.out.println(userRecord);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        // click listeners
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get email / password
                String email = emailInput.getText().toString();
                String password = pwInput.getText().toString();
                signIn(email, password);
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

    public void signIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(SignIn.this, "Welcome back", Toast.LENGTH_SHORT).show();

                            //Intent intent = new Intent(SignIn.this, MainActivity.class);
                            Intent intent = new Intent(SignIn.this, ListActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(SignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });

    }
}