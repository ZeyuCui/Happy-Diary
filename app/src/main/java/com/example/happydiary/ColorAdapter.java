package com.example.happydiary;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happydiaryy.R;

import java.util.List;


public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.MyVH> {
    Context context;
    List<Integer> list;
    int index = 0;

    public ColorAdapter(Context context, List<Integer> list) {
        this.context = context;
        this.list = list;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @NonNull
    @Override
    public MyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyVH(LayoutInflater.from(context).inflate(R.layout.item_theme,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyVH holder, int position) {
        holder.mItem.setBackgroundColor(context.getColor(list.get(position)));
        holder.mItem.setOnClickListener(view -> {
            if(position<index){
                SP.getInstance(context).saveInt("theme", list.get(position));
                ((Activity)context).finish();
            }else {
                Toast.makeText(context,"This theme hasn't been locked yet",Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyVH extends RecyclerView.ViewHolder{
        ConstraintLayout mItem;

        public MyVH(@NonNull View itemView) {
            super(itemView);
            mItem = itemView.findViewById(R.id.mItem);
        }
    }
}
