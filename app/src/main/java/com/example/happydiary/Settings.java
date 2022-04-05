package com.example.happydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.happydiaryy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Settings extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;

    private EditText editName;
    private EditText inputNewPW;
    private EditText inputVerifyNewPW;
    private TextView pwError;

    ConstraintLayout mView;
    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        mView = findViewById(R.id.mView);
        btn=(Button) findViewById(R.id.btn_theme);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 查询当签到的数量
                int size = SP.getInstance(getApplicationContext()).queryRecordList().size();
                Log.d("===", "size:"+size);

                Intent intent=new Intent(Settings.this,ChangeThemeActivity.class);
                intent.putExtra("size",size);
                startActivity(intent);
            }
        });




        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        // back button
        ImageView backToMain = (ImageView) findViewById(R.id.icon_back);
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageView logout = (ImageView) findViewById(R.id.icon_logoutSettings);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Settings.this, SignIn.class);
                startActivity(intent);
            }
        });

        // need help text
        TextView needHelp = (TextView)findViewById(R.id.helpLink);
        needHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launch tour 
            }
        });

        // set name edit
        editName = (EditText) findViewById(R.id.editName);
        editName.setText(user.getDisplayName());

        // set email
        TextView email = (TextView) findViewById(R.id.emailDisplay);
        email.setText(user.getEmail());

        // reset pw
        inputNewPW = (EditText) findViewById(R.id.input_newPW);
        inputVerifyNewPW = (EditText) findViewById(R.id.inputVerifyNewPW);
        pwError = (TextView) findViewById(R.id.matchErrorSettings);
        pwError.setVisibility(View.INVISIBLE);

        // save
        Button saveButton = (Button) findViewById(R.id.saveSettings);
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
        String newPW = inputNewPW.getText().toString();
        String verifyPW = inputVerifyNewPW.getText().toString();

        if((!newPW.isEmpty()) && (verifyPW.isEmpty())) {
            Toast.makeText(Settings.this, "Verify password field is required", Toast.LENGTH_LONG).show();
        }

        else if((nameInput != user.getDisplayName()) || ((!newPW.isEmpty()) && (!verifyPW.isEmpty()))) {
            // if there is a change, update
            if(nameInput != user.getDisplayName()) {
                UserProfileChangeRequest updates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(nameInput).build();
                user.updateProfile(updates);

            }

            // update pw if applicable
            if((!newPW.isEmpty()) && (!verifyPW.isEmpty())) {
                // check that new and verify are same
                if(newPW.equals(verifyPW)) {
                    //set error invisible if previously visible
                    pwError.setVisibility(View.INVISIBLE);
                    // update pw
                    user.updatePassword(newPW).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // blank out fields
                            inputNewPW.getText().clear();
                            inputVerifyNewPW.getText().clear();
                        }
                    });

                } else {
                    pwError.setVisibility(View.VISIBLE);
                }

            }


            // changes saved
            Toast.makeText(Settings.this, "Changes have been saved", Toast.LENGTH_LONG).show();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mView.setBackgroundColor(getColor(SP.getInstance(this).getInt("theme", R.color.white)));
    }


}