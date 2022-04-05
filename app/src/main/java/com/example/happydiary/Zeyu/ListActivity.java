package com.example.happydiary.Zeyu;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.happydiary.SignIn;
import com.example.happydiaryy.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ListActivity extends AppCompatActivity {


//    FloatingActionButton myfabutton;
//    private FirebaseAuth mAuth;
//    FirebaseUser firebaseUser;
//    FirebaseFirestore firebaseFirestore;
//
//    FirestoreRecyclerAdapter<firebasemodel,NoteViewHolder> diaryAdapter;
//    RecyclerView mrecyclerview;
//    StaggeredGridLayoutManager staggeredGridLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
//
//        mAuth =FirebaseAuth.getInstance();
//        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
//        firebaseFirestore=FirebaseFirestore.getInstance();
//
//        myfabutton =findViewById(R.id.createnotefab);
//
//
//        //getSupportActionBar().setTitle("Full");
//
//        myfabutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(ListActivity.this,CreateActivity.class));
//
//            }
//        });
//
//
//        Query query=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("Full").orderBy("title",Query.Direction.ASCENDING);
//
//        FirestoreRecyclerOptions<firebasemodel> usernotes= new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query,firebasemodel.class).build();
//
//        diaryAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(usernotes) {
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull firebasemodel firebasemodel) {
//
//
//                ImageView menuButton=noteViewHolder.itemView.findViewById(R.id.itemmenu);
//
//                int colourcode=getRandomColor();
//                noteViewHolder.mdiary.setBackgroundColor(noteViewHolder.itemView.getResources().getColor(colourcode,null));
//
//                noteViewHolder.diaryTitle.setText(firebasemodel.getTitle());
//                noteViewHolder.diaryContent.setText(firebasemodel.getContent());
//
//                String docId= diaryAdapter.getSnapshots().getSnapshot(i).getId();
//
//                noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //we have to open note detail activity
//
//
//                        Intent intent=new Intent(v.getContext(), ReadActivity.class);
//                        intent.putExtra("title",firebasemodel.getTitle());
//                        intent.putExtra("content",firebasemodel.getContent());
//                        intent.putExtra("noteId",docId);
//
//                        v.getContext().startActivity(intent);
//
//                       // Toast.makeText(getApplicationContext(),"This is Clicked",Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//
//                menuButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        PopupMenu popMenu=new PopupMenu(v.getContext(),v);
//                        popMenu.setGravity(Gravity.END);
//                        popMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                            @Override
//                            public boolean onMenuItemClick(MenuItem item) {
//
//                                Intent intent=new Intent(v.getContext(),CreateActivity.class);
//                                intent.putExtra("title",firebasemodel.getTitle());
//                                intent.putExtra("content",firebasemodel.getContent());
//                                intent.putExtra("noteId",docId);
//                                v.getContext().startActivity(intent);
//                                return false;
//                            }
//                        });
//
//                        popMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                            @Override
//                            public boolean onMenuItemClick(MenuItem item) {
//                                //Toast.makeText(v.getContext(),"This note is deleted",Toast.LENGTH_SHORT).show();
//                                DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("Full").document(docId);
//                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        Toast.makeText(v.getContext(),"This note is deleted",Toast.LENGTH_SHORT).show();
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(v.getContext(),"Failed To Delete",Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//
//
//                                return false;
//                            }
//                        });
//
//                        popMenu.show();
//                    }
//                });
//
//
//            }
//
//            @NonNull
//            @Override
//            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//               View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_layout,parent,false);
//               return new NoteViewHolder(view);
//            }
//        };
//
//
//        mrecyclerview=findViewById(R.id.recyclerview);
//        mrecyclerview.setHasFixedSize(true);
//        staggeredGridLayoutManager=new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
//        mrecyclerview.setLayoutManager(staggeredGridLayoutManager);
//        mrecyclerview.setAdapter(diaryAdapter);
//
//
//    }
//
//    public class NoteViewHolder extends RecyclerView.ViewHolder
//    {
//        private TextView diaryTitle;
//        private TextView diaryContent;
//        LinearLayout mdiary;
//
//        public NoteViewHolder(@NonNull View itemView) {
//            super(itemView);
//            diaryTitle =itemView.findViewById(R.id.dairytitle);
//            diaryContent =itemView.findViewById(R.id.diarycontent);
//            mdiary =itemView.findViewById(R.id.diary);
//
//
//        }
//    }
//
//
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.menu,menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId())
//        {
//            case R.id.logout:
//                mAuth.signOut();
//                finish();
//                startActivity(new Intent(ListActivity.this, SignIn.class));
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        diaryAdapter.startListening();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//       if(diaryAdapter !=null)
//       {
//           diaryAdapter.stopListening();
//       }
//    }
//
//
//    private int getRandomColor()
//    {
//        List<Integer> colorcode=new ArrayList<>();
//        colorcode.add(R.color.pink1);
//        colorcode.add(R.color.pink2);
//        colorcode.add(R.color.pink3);
//
//        Random random=new Random();
//        int number=random.nextInt(colorcode.size());
//        return colorcode.get(number);
//
//
//
    }
}