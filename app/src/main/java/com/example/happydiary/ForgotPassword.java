package com.example.happydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.happydiaryy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class ForgotPassword extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText emailInput;
    private Button reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        ImageView back = (ImageView)findViewById(R.id.icon_backSettings);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        emailInput = (EditText) findViewById(R.id.input_email_resetPW);
        reset = (Button) findViewById(R.id.button_resetPW);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailInput.getText().toString();

                if(email.isEmpty()) {
                    Toast.makeText(ForgotPassword.this, "Input email", Toast.LENGTH_SHORT).show();
                }
                else {
                    resetPW(email);
                }

            }
        });

    }

    protected void resetPW(String email) {

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(ForgotPassword.this, "Email sent", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}