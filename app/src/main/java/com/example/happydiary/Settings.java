package com.example.happydiary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.happydiaryy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Settings extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;

    private ImageView backToMain;
    private EditText editName;
    private TextView email;
    private EditText inputNewPW;
    private EditText inputVerifyNewPW;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        // back button
        backToMain = (ImageView) findViewById(R.id.icon_back);
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // set name edit
        editName = (EditText) findViewById(R.id.editName);
        editName.setText(user.getDisplayName());

        // set email
        email = (TextView) findViewById(R.id.emailDisplay);
        email.setText(user.getEmail());

        // reset pw
        inputNewPW = (EditText) findViewById(R.id.input_newPW);
        inputVerifyNewPW = (EditText) findViewById(R.id.inputVerifyNewPW);

        // save
        saveButton = (Button) findViewById(R.id.saveSettings);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

    }

    protected void save() {
        // update name
        String nameInput = editName.getText().toString();

        // if there is a change, update
        if(nameInput != user.getDisplayName()) {
            UserProfileChangeRequest updates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nameInput).build();
            user.updateProfile(updates);

        }

        // update pw if applicable

        // changes saved
        Toast.makeText(Settings.this, "Changes have been saved", Toast.LENGTH_LONG).show();
    }

    protected void verifyPW() {

    }
}