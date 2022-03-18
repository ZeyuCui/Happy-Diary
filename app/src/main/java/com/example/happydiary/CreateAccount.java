package com.example.happydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.happydiary.Zeyu.CreateActivity;
import com.example.happydiary.Zeyu.ListActivity;
import com.example.happydiaryy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateAccount extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase root;
    DatabaseReference reference;

    private EditText nameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText verifyPWInput;
    private TextView verifyError;
    private TextView emailError;
    private Button createBtn;
    private String date="01 Dec 2021";
    private String location="Happy Island";
    private String userguidance="Welcome to Happy Diary. Here, you can store all of your memories and keep track of those ups and downs by tag.\n" +
            "\n" +
            "Here are some suggestions:\n" +
            "\n" +
            "1. Record your experience using tags like the example below. Click save, then Happy Diary will generate four versions of your diary: Full, happy, motivated, and appreciate.\n" +
            "\n" +
            "2. Review your diaries by tags. On your home page, you have three favorite tags, and you can change them on your setting page. You can delete and edit your diary whenever you want.\n" +
            "\n" +
            "3. Click the location button on the writing page, record your footsteps.\n" +
            "\n" +
            "\n" +
            "\n" +
            "<happy>\n" +
            "I am happy to take 5520 Mobile Application\n" +
            "\n" +
            "<motivated>\n" +
            "I have learned a lot of knowledge on developing through this course.\n" +
            "\n" +
            "<appreciate>\n" +
            "Thanks for all the introductors' help, thanks for your teaching and advice.\n" +
            "Thanks to my teammates, I couldn't finish the project without you.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // set up page elements
        mAuth = FirebaseAuth.getInstance();

        nameInput = (EditText) findViewById(R.id.input_name);
        emailInput = (EditText) findViewById(R.id.input_email_createAcc);
        passwordInput = (EditText) findViewById(R.id.input_password_createAccount);
        createBtn = (Button) findViewById(R.id.button_createAccount);
        verifyPWInput = (EditText) findViewById(R.id.input_verifyPW);
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        root= FirebaseDatabase.getInstance();
        reference=root.getReference("userdate");

        // create and hide error to start
        verifyError = (TextView) findViewById(R.id.matchError);
        verifyError.setVisibility(View.INVISIBLE);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get account info
                String name = nameInput.getText().toString();
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                String verifyPW = verifyPWInput.getText().toString();

                // check input is not empty
                if((name.isEmpty()) || (email.isEmpty()) || (password.isEmpty()) || (verifyPW.isEmpty())) {
                    Toast.makeText(CreateAccount.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                // check password and verify are same
                else if(!password.equals(verifyPW)) {
                    verifyError.setVisibility(View.VISIBLE);
                }
                // success
                else {
                    verifyError.setVisibility(View.INVISIBLE);
                    createAccount(email, password, name);
                }

            }
        });
    }

    private void createAccount(String email, String password, String name) {

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            //Log.d(TAG, "createWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // set their name - firebase display name
                            UserProfileChangeRequest updates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build();
                            user.updateProfile(updates);

                            // send verification email
                            user.sendEmailVerification()
                                    .addOnCompleteListener(CreateAccount.this, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // confirm email sent, instruct user
                                            Toast.makeText(CreateAccount.this, "A verification link has been sent to your email", Toast.LENGTH_LONG).show();

                                            //create new user guidance
                                            DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("Full").document();
                                            Map<String, Object> diary = new HashMap<>();
                                            diary.put("title", date);
                                            diary.put("location",location);
                                            diary.put("content", userguidance);
                                            diary.put("userid", firebaseUser.getUid());
                                            documentReference.set(diary);

                                            if (userguidance.contains("<") && userguidance.contains(">")){
                                                String[] sKey = StringUtils.substringsBetween(userguidance, "<", ">");
                                                String[] sValue = StringUtils.substringsBetween(userguidance, ">", "<");
                                                String last = StringUtils.substringAfterLast(userguidance, ">");
                                                for (int i = 0; i < sKey.length; i++) {
                                                    documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection(sKey[i]).document();
                                                    DocumentReference referenceTag= firebaseFirestore.collection("tags").document(firebaseUser.getUid()).collection("tag").document();
                                                    Map<String, Object> tag = new HashMap<>();
                                                    tag.put("tagvalue",sKey[i]);
                                                    referenceTag.set(tag);
                                                    diary = new HashMap<>();
                                                    diary.put("title", date);
                                                    diary.put("location", location);
                                                    diary.put("userid", firebaseUser.getUid());
                                                    if (i < sKey.length - 1)
                                                        diary.put("content", sValue[i]);
                                                    else
                                                        diary.put("content", last);
                                                    documentReference.set(diary);}}
                                            reference.child(firebaseUser.getUid()).setValue(date+",");

                                            // back to sign in
                                            finish();
                                        }
                                    });

                        }
                        else {
                            Toast.makeText(CreateAccount.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }

                });

    }
}