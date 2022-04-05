package com.example.happydiary;

import android.app.usage.NetworkStats;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.example.happydiaryy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.myViewHolder>{
    private ArrayList<String> title;
    private ArrayList<String> content;
    private ArrayList<String> location;
    private ArrayList<String> docId;
    private String tag;
    private OnListListner onListListner;

    public ListAdapter(ArrayList<String> title, ArrayList<String> content, ArrayList<String> location, ArrayList<String> docId, String tag, OnListListner onListListner){
        System.out.println(title.size());
        this.title = title;
        this.content = content;
        this.location = location;
        this.docId = docId;
        this.tag = tag;
        this.onListListner = onListListner;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_adapter,parent,false), this.onListListner);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        int colourcode=getRandomColor();
        holder.mdiary.setBackgroundColor(holder.itemView.getResources().getColor(colourcode,null));

        holder.diaryTitle.setText(title.get(position));
        holder.diaryContent.setText(content.get(position));
        holder.title = title.get(position);
        holder.content = content.get(position);
        holder.location = location.get(position);
        holder.docId = docId.get(position);
        holder.tag = tag;

    }

    @Override
    public int getItemCount() {
        System.out.println(title.size());
        return title.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView diaryTitle;
        TextView diaryContent;
        LinearLayout mdiary;
        OnListListner onListListner;
        ImageView menuButton;
        String title;
        String content;
        String location;
        String docId;
        String tag;

        public myViewHolder(@NonNull View itemView, OnListListner onListListner) {
            super(itemView);
            diaryTitle = itemView.findViewById(R.id.dairytitle);
            diaryContent = itemView.findViewById(R.id.diarycontent);
            mdiary =itemView.findViewById(R.id.diary);
            menuButton=itemView.findViewById(R.id.itemmenu);
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popMenu=new PopupMenu(view.getContext(),view);
                    popMenu.setGravity(Gravity.END);
                    popMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            Intent intent=new Intent(view.getContext(),EditActivity.class);
                            intent.putExtra("title", title);
                            intent.putExtra("content", content);
                            intent.putExtra("location", location);
                            intent.putExtra("noteId", docId);
                            intent.putExtra("tag",tag);
                            view.getContext().startActivity(intent);
                            return false;
                        }
                    });

                    popMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            //Toast.makeText(v.getContext(),"This note is deleted",Toast.LENGTH_SHORT).show();
                           /* DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("Full").document(docId);
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        System.out.println("Delete");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        System.out.println("Not Deleted");
                                    }
                                });*/
                            DocumentReference documentReferencetag=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection(tag).document(docId);
                            documentReferencetag.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    System.out.println("Delete");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    System.out.println("Not Deleted");
                                }
                            });

                            return false;
                        }
                    });

                    popMenu.show();
                }
            });
            this.onListListner = onListListner;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            onListListner.onListClick(title, content, location, docId, getAdapterPosition());

        }
    }
    public interface OnListListner{

        void onListClick(String title, String content, String location, String docId, int adapterPosition);
    }

    private int getRandomColor()
    {
        List<Integer> colorcode=new ArrayList<>();
        colorcode.add(R.color.cornsilk);
        colorcode.add(R.color.peach);
        colorcode.add(R.color.cream);

        Random random=new Random();
        int number=random.nextInt(colorcode.size());
        return colorcode.get(number);
    }
}
