package com.example.happydiary;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SP {

    private final String RECORD_LIST = "RECORD_LIST";

    private static SP sp;

    private SharedPreferences sharedPreferences;

    private SP(Context context) {
        sharedPreferences = context.getSharedPreferences("RECORD",Context.MODE_PRIVATE);
    }

    public static SP getInstance(Context context) {
        if (null == sp) {
            sp = new SP(context);
        }
        return sp;
    }

    public void saveInt(String key,int value){
        sharedPreferences.edit().putInt(key,value).commit();
    }

    public int getInt(String key,int def){
        return sharedPreferences.getInt(key,def);
    }

    public void saveRecordList(BaseDateEntity entity){
        // 如果记录为空，直接保存
        String str = sharedPreferences.getString(RECORD_LIST,"");
        if(TextUtils.isEmpty(str)) {
            sharedPreferences.edit().putString(RECORD_LIST,new StringBuilder()
                    .append(new Gson().toJson(entity))
                    .append(";").toString()).apply();
        } else {
            // 如果有记录，直接加到记录的最后
            sharedPreferences.edit().putString(RECORD_LIST,new StringBuilder().append(str)
                    .append(new Gson().toJson(entity))
                    .append(";").toString()).apply();
        }
    }


    public ArrayList<BaseDateEntity> queryRecordList(){
        ArrayList<BaseDateEntity> list = new ArrayList<>();
        String str = sharedPreferences.getString(RECORD_LIST,"");
        if(TextUtils.isEmpty(str)) {
            return list;
        } else {
            List<String> listTmp = Arrays.asList(str.split(";"));
            for(String strTmp : listTmp) {
                BaseDateEntity entity = new Gson().fromJson(strTmp,BaseDateEntity.class);
                list.add(entity);
            }
        }
        return list;
    }
}