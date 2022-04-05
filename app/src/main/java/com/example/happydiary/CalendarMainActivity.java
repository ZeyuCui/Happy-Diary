package com.example.happydiary;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happydiaryy.R;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarMainActivity extends AppCompatActivity {

    private TextView tvDate;
    private com.example.happydiary.CalendarRecycleView rcDate;
    private Button rlSignK;
    private ArrayList<BaseDateEntity> list;
    private RecyclerView rcList;
    LinearLayout mView;

    //private DatabaseReference rootDatabaseref;
    Button btn_home;
    //Button btn_reward;
    int Count=0 ;
    Intent intent;

    @Override
    protected void onResume() {
        super.onResume();
        mView.setBackgroundColor(getColor(SP.getInstance(this).getInt("theme", R.color.white)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_calendar_main);
        initView();
        initData();
        initEvent();

        //btn_reward=(Button) findViewById(R.id.reward);
        btn_home=(Button) findViewById(R.id.gohome);
        mView = findViewById(R.id.mView);

//        rootDatabaseref= FirebaseDatabase.getInstance().getReference();
//
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");



//        btn_reward.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 查询当签到的数量
//                int size = SP.getInstance(getApplicationContext()).queryRecordList().size();
//                Log.d("===", "size:"+size);
//
//                btn_reward.setClickable(true);
//                btn_reward.setEnabled(true);
//                Intent intent=new Intent(CalendarMainActivity.this, ChangeThemeActivity.class);
//                intent.putExtra("size",size);
//                startActivity(intent);
//
//            }
//        });

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rootDatabaseref.setValue("hello!");
                //myRef.setValue("Hello, World!");
                Intent intent=new Intent(CalendarMainActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }


    private void initView() {
        tvDate = findViewById(R.id.tv_date);
        rcDate = findViewById(R.id.recycle_view);
        rlSignK = findViewById(R.id.rl_sign_k);
        rcList = findViewById(R.id.rc_list);

    }

    private void initData() {
        Calendar calendar = Calendar.getInstance();
        tvDate.setText(calendar.get(Calendar.YEAR) + "Year" + (calendar.get(Calendar.MONTH) + 1) + "Month");

        list = SP.getInstance(getApplicationContext()).queryRecordList();
        rcDate.initRecordList(list);


        LinearLayoutManager manager = new LinearLayoutManager(CalendarMainActivity.this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcList.setLayoutManager(manager);
    }

    private void initEvent() {

        rcDate.setOnCalendarDateListener(new OnCalendarDateListener() {
            @Override
            public void onDateChange(Point nowCalendar, int startDay, int endDay, boolean startBelong, boolean endBelong) {
                tvDate.setText(nowCalendar.x + "Year" + nowCalendar.y + "Month");
            }

            @Override
            public void onDateItemClick(DateEntity dateEntity) {
                //Toast.makeText(MainActivity.this, "点击日期 " + dateEntity.date, Toast.LENGTH_SHORT).show();
            }
        });

        rlSignK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                BaseDateEntity now = new BaseDateEntity(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1),calendar.get(Calendar.DAY_OF_MONTH));
                // 查询当前是否有记录，如果有记录 提示 无需重复签到
                ArrayList<BaseDateEntity> list  = SP.getInstance(getApplicationContext()).queryRecordList();
                for(BaseDateEntity entity : list) {
                    if(TextUtils.equals(entity.toString(),now.toString())){
                        Toast.makeText(CalendarMainActivity.this, "You've already check-in today!" , Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // 如果没有记录，进行签到
                list.add(now);
                Log.e("XXP", calendar.get(Calendar.YEAR)+"--"+(calendar.get(Calendar.MONTH) + 1)+"---"+calendar.get(Calendar.DAY_OF_MONTH));
                rcDate.initRecordList(list);

                // 存入SP
                SP.getInstance(getApplicationContext()).saveRecordList(now);
                Toast.makeText(CalendarMainActivity.this, "Successfully Check in! " , Toast.LENGTH_SHORT).show();

                //rootDatabaseref.setValue(calendar);


            }
        });
    }

    //设置可以点击日期的事件记录
    private ArrayList initRecordList() {
        ArrayList<BaseDateEntity> list = new ArrayList();

        return list;
    }

}