package com.example.happydiary;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happydiaryy.R;

import java.util.ArrayList;
import java.util.List;

public class ChangeThemeActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String prefs = "THEME";
    RecyclerView mRecyclerView;
    ColorAdapter adapter;
    int size;
    List<Integer> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_theme);

        mRecyclerView = findViewById(R.id.mRecyclerView);

        sharedPreferences = getSharedPreferences(prefs, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        size = getIntent().getIntExtra("size",0);

        if(size==0){
            list.add(Config.colorList.get(0));
        }else {
            int num = (size+1)/2+1;// (size+1)/2+1
            for(int i=0;i<num;i++){
                if(i<Config.colorList.size()){
                    list.add(Config.colorList.get(i));
                }
            }
        }

        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        adapter = new ColorAdapter(this, Config.colorList);
        adapter.setIndex(list.size());
        mRecyclerView.setAdapter(adapter);


    }

}