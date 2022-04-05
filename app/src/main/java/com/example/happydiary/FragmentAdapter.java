package com.example.happydiary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapter extends FragmentStateAdapter {
    private String tag1;
    private String tag2;
    private String tag3;
    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String tag1, String tag2, String tag3) {
        super(fragmentManager, lifecycle);
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                Fragment sf = new SecondFragment();
                Bundle data2 = new Bundle();
                data2.putString("tag",tag2);
                sf.setArguments(data2);
                return sf;
            case 2:
                Fragment tf = new ThirdFragment();
                Bundle data3 = new Bundle();
                data3.putString("tag",tag3);
                tf.setArguments(data3);
                return tf;
        }
        Fragment ff = new FirstFragment();
        Bundle data1 = new Bundle();
        data1.putString("tag",tag1);
        ff.setArguments(data1);
        return ff;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
