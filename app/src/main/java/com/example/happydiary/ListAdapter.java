package com.example.happydiary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happydiaryy.R;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.myViewHolder>{
    String[] list;

    public ListAdapter(String[] list){
        this.list = list;
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.dateView.setText(list[position]);
        holder.contentView.setText(list[position]);
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    static class myViewHolder extends RecyclerView.ViewHolder {
        TextView dateView;
        TextView contentView;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            dateView = itemView.findViewById(R.id.itemDate);
            contentView = itemView.findViewById(R.id.itemContent);


        }
    }
}
