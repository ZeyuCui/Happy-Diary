package com.example.happydiary;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.happydiaryy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;




public class FirstFragment extends Fragment implements ListAdapter.OnListListner{

    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private ListAdapter listAdapter;
    private ArrayList<String> title=new ArrayList<String>();
    private ArrayList<String> content = new ArrayList<String>();
    private ArrayList<String> location = new ArrayList<String>();
    private ArrayList<String> docId = new ArrayList<String>();
    private String tag;
    private ArrayList<String> tags = new ArrayList<String>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListAdapter.OnListListner onListListner;
    private FloatingActionButton myfabutton;
    String userRecord;
    FirebaseDatabase root;
    DatabaseReference reference;

    TextView hint;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore= FirebaseFirestore.getInstance();
        Toast toast=Toast. makeText(getContext(), "Please swipe to refresh the content!",Toast. LENGTH_SHORT);



        Bundle data = getArguments();
        if (data != null){
            tag = data.getString("tag");
        }


//        CollectionReference noteCollectionReference=

        Query query = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection(tag);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                content.clear();
                title.clear();
                location.clear();
                docId.clear();
                System.out.println("Hello");
                if (task.isSuccessful()){
                    System.out.println("Query Successful");
                    for(QueryDocumentSnapshot document:task.getResult()){
                        content.add(document.getData().get("content").toString());
                        title.add(document.getData().get("title").toString());
                        location.add(document.getData().get("location").toString());
                        docId.add(document.getId().toString());
                    }

                }
                else{
                    System.out.println("Query Failed");
                }

            }

        });
        System.out.println(title);
        onListListner = this;
        listAdapter = new ListAdapter(title, content,location,docId,tag,onListListner);

        View view = inflater.inflate(R.layout.fragment_first, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycleView1);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        Date date=new Date();
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd MMM yyyy");
        String today=dateFormat.format(date);
        root= FirebaseDatabase.getInstance();
        reference=root.getReference("userdate");
        /*reference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
                userRecord=snapshot.getValue().toString();
                System.out.println(userRecord);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        myfabutton = view.findViewById(R.id.floatingActionButton);
        myfabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if(userRecord.contains(today)){
                    //Toast.makeText(view.getContext(),"you've already create a diary today",Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(getContext())
                            .setMessage("You've already created a diary today, do you want to edit it?")
                            .setCancelable(false)
                            .setPositiveButton("OK", new
                                    DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                            //open edit today's full
                                        }
                                    })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
                else{
                    startActivity(new Intent(getActivity(),CreateActivity.class));
                }*/
                startActivity(new Intent(getActivity(),CreateActivity.class));

            }
        });

        hint=view.findViewById(R.id.hinttext);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout1);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Query query = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection(tag).orderBy("title",Query.Direction.DESCENDING);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        content.clear();
                        title.clear();
                        location.clear();
                        docId.clear();
                        System.out.println("Hello");
                        if (task.isSuccessful()){
                            System.out.println("Query Successful");
                            for(QueryDocumentSnapshot document:task.getResult()){
                                content.add(document.getData().get("content").toString());
                                title.add(document.getData().get("title").toString());
                                location.add(document.getData().get("location").toString());
                                docId.add(document.getId().toString());
                            }

                        }
                        else{
                            System.out.println("Query Failed");
                        }

                    }

                });
                listAdapter = new ListAdapter(title, content,location,docId,tag,onListListner);
                recyclerView.setAdapter(listAdapter);

                swipeRefreshLayout.setRefreshing(false);
                hint.setVisibility(View.INVISIBLE);
            }
        });
        return view;
    }

    @Override
    public void onListClick(String title, String content,String location, String docId, int position) {
        Intent intent = new Intent(this.getContext(), ReadActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("location", location);
        intent.putExtra("noteId", docId);
        intent.putExtra("tag",tag);
        startActivity(intent);
    }


}