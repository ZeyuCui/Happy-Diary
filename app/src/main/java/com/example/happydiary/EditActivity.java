package com.example.happydiary;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.happydiary.Zeyu.firebasemodel;
import com.example.happydiaryy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    Intent data;
    EditText mtitleEdit, mcontentEdit;
    TextView mLocationEdit;
    FloatingActionButton mSaveEdit;
    ImageView backimg;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    ArrayList<firebasemodel> mDiaries = new ArrayList<>();
    String newFull;


    RelativeLayout mView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mView=findViewById(R.id.mView);

        mtitleEdit =findViewById(R.id.editTitle);
        mcontentEdit =findViewById(R.id.editContent);
        mSaveEdit =findViewById(R.id.saveEdit);
        mLocationEdit = findViewById(R.id.editLocation);

        data=getIntent();

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();


        Toolbar toolbar=findViewById(R.id.toolbarEdit);
        backimg=findViewById(R.id.icon_back);
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });




        mSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"savebuton click",Toast.LENGTH_SHORT).show();

                String newtitle= data.getStringExtra("title");
                String newcontent= mcontentEdit.getText().toString();
                String newloc = data.getStringExtra("location");
                String originconten = data.getStringExtra("content");


                if(newtitle.isEmpty()||newcontent.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Something is empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                //new title and content are not empty
                //replace the original one
                else
                {
                    String type = data.getStringExtra("tag");

                    DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection(type).document(data.getStringExtra("noteId"));
                    Map<String,Object> note=new HashMap<>();
                    note.put("title",newtitle);
                    note.put("content",newcontent);
                    note.put("location",newloc);
                    note.put("userid",firebaseUser.getUid());
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Note is updated",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditActivity.this,MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed To update",Toast.LENGTH_SHORT).show();
                        }
                    });
                    //solve conflicts when it's under full field
                    if(type.equals("Full")&&(newcontent.contains("<"))&&(newcontent.contains(">"))){


                        String[] sKey= StringUtils.substringsBetween(newcontent, "<", ">");
                        String[] sValue= StringUtils.substringsBetween(newcontent, ">", "<");
                        String last = StringUtils.substringAfterLast(newcontent,">");
                        //delete all the sub notes of this day

                      if(originconten.contains("<")&& originconten.contains(">")){

                            String[] oldKey= StringUtils.substringsBetween(originconten, "<", ">");
                            String[] oldValue= StringUtils.substringsBetween(originconten, ">", "<");
                            String oldlast = StringUtils.substringAfterLast(originconten,">");
                        for(int i=0;i<oldKey.length;i++)
                        {
                            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                            CollectionReference itemsRef = rootRef.collection("notes");
                            Query query = itemsRef.document(firebaseUser.getUid()).collection(oldKey[i]).whereEqualTo("title",newtitle);
                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (DocumentSnapshot document : task.getResult()) {
                                            itemsRef.document(document.getId()).delete();
                                        }
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });


                        }}
                        //create new sub notes of this day
                        for(int i=0;i<sKey.length;i++)
                        {
                            documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection(sKey[i]).document(data.getStringExtra("noteId"));
                            DocumentReference referenceTag= firebaseFirestore.collection("tags").document(firebaseUser.getUid()).collection("tag").document();
                            Map<String, Object> tag = new HashMap<>();
                            tag.put("tagvalue",sKey[i]);
                            referenceTag.set(tag);
                            note= new HashMap<>();
                            note.put("title",newtitle);
                            note.put("location",newloc);
                            note.put("userid",firebaseUser.getUid());
                            if(i<sKey.length-1)
                                note.put("content",sValue[i]);
                            else
                                note.put("content",last);
                            documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Toast.makeText(getApplicationContext(),"Note Created Succesffuly",Toast.LENGTH_SHORT).show();
                                    //startActivity(new Intent(createnote.this,notesactivity.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //  Toast.makeText(getApplicationContext(),"Failed To Create Note",Toast.LENGTH_SHORT).show();
                                    // startActivity(new Intent(createnote.this,notesactivity.class));
                                }
                            });

                        }
                    }
                    //solve conflicts when it's under sub field
                    else{
                       /* FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                        CollectionReference itemsRef = rootRef.collection("notes");
                        Query query = itemsRef.document(firebaseUser.getUid()).collection("Full").whereEqualTo("title",newtitle);
                        Task<QuerySnapshot> task=query.get();
                        QuerySnapshot documents=task.getResult();
                          /*  firebasemodel todayFull=documents.toObject(firebasemodel.class);
                            String changedContent=todayFull.getContent();
                            String[] schange= StringUtils.substringsBetween(changedContent, "<"+type, "<");
                            String replacement="<"+type+newcontent+"<";
                            newFull= changedContent.replaceAll(schange[0],replacement);

                        }
                        //delete this day's full
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        itemsRef.document(document.getId()).delete();
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                        //create new full
                        documentReference=itemsRef.document(firebaseUser.getUid()).collection("Full").document(data.getStringExtra("noteId"));
                        note= new HashMap<>();
                        note.put("title",newtitle);
                        note.put("location",newloc);
                        note.put("content",newFull);
                        documentReference.set(note);*/
                    }


                }

            }
        });


        String diarytitle=data.getStringExtra("title");
        String diarycontent=data.getStringExtra("content");
        String diaryloc=data.getStringExtra("location");
        mcontentEdit.setText(diarycontent);
        mtitleEdit.setText(diarytitle);
        mLocationEdit.setText(diaryloc);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mView.setBackgroundColor(getColor(SP.getInstance(this).getInt("theme", R.color.white)));
    }
}