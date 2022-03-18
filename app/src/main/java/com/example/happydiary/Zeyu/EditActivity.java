package com.example.happydiary.Zeyu;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

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

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    ArrayList<firebasemodel> mDiaries = new ArrayList<>();
    String newFull;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mtitleEdit =findViewById(R.id.editTitle);
        mcontentEdit =findViewById(R.id.editContent);
        mSaveEdit =findViewById(R.id.saveEdit);
        mLocationEdit = findViewById(R.id.editLocation);

        data=getIntent();

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();


        Toolbar toolbar=findViewById(R.id.toolbarEdit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"savebuton click",Toast.LENGTH_SHORT).show();

                String newtitle= mtitleEdit.getText().toString();
                String newcontent= mcontentEdit.getText().toString();
                String newloc = data.getStringExtra("location");


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
                            startActivity(new Intent(EditActivity.this,ListActivity.class));
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
                        //delete all the sub notes of this day's old diary
                        for(int i=0;i<sKey.length;i++)
                        {
                            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                            CollectionReference itemsRef = rootRef.collection("notes");
                            Query query = itemsRef.document(firebaseUser.getUid()).collection(sKey[i]).whereEqualTo("title",newtitle);
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


                        }
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}