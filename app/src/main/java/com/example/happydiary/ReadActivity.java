package com.example.happydiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import android.widget.RelativeLayout;

import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.happydiaryy.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ReadActivity extends AppCompatActivity {


    private TextView mtitleRead, mcontentRead,mLocationRead;
    FloatingActionButton editButton;
    ImageView backimg;

    RelativeLayout mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        mtitleRead =findViewById(R.id.titleRead);
        mcontentRead =findViewById(R.id.contentRead);
        editButton =findViewById(R.id.readDiary);
        mLocationRead = findViewById(R.id.readLocation);
        mView=findViewById(R.id.mView);
        Toolbar toolbar=findViewById(R.id.toolbarRead);
        backimg=findViewById(R.id.icon_back);
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent data=getIntent();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),EditActivity.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("content",data.getStringExtra("content"));
                intent.putExtra("noteId",data.getStringExtra("noteId"));
                intent.putExtra("tag",data.getStringExtra("tag"));
                intent.putExtra("location",data.getStringExtra("location"));
                v.getContext().startActivity(intent);
            }
        });

        mcontentRead.setText(data.getStringExtra("content"));
        mtitleRead.setText(data.getStringExtra("title"));
        mLocationRead.setText(data.getStringExtra("location"));

    }




    @Override
    protected void onResume() {
        super.onResume();
        mView.setBackgroundColor(getColor(SP.getInstance(this).getInt("theme", R.color.white)));
    }

}