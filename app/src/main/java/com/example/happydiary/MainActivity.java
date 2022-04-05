package com.example.happydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.happydiaryy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private Button tabBtn;
    private String tagSelected1="Full";
    private String tagSelected2="happy";
    private String tagSelected3="appreciate";
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<String> tags = new ArrayList<String>();
    private String[] tagsA;
    private HashSet set = new HashSet();
    LinearLayout mView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mView=findViewById(R.id.mView);


        Toast toast=Toast. makeText(this, "Please swipe to refresh the content!",Toast. LENGTH_SHORT);

        ImageView logout = (ImageView) findViewById(R.id.icon_logout);
        logout.setOnClickListener(view -> logout());

        ImageView settings = findViewById(R.id.icon_settings);
        settings.setOnClickListener(view -> {
            Intent set = new Intent(MainActivity.this, Settings.class);
            startActivity(set);
        });
        Intent intent = getIntent();

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore= FirebaseFirestore.getInstance();

        Query query = firebaseFirestore.collection("tags").document(firebaseUser.getUid()).collection("tag");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                tags.clear();
                set.clear();
                tags.add("Full");
                System.out.println("Hello");
                if (task.isSuccessful()){
                    System.out.println("Query Successful");
                    for(QueryDocumentSnapshot document:task.getResult()){
                        String temp = document.getData().get("tagvalue").toString();
                        if (!set.contains(temp)){
                            set.add(temp);
                            tags.add(temp);
                        }
                    }


                }
                else{
                    System.out.println("Query Failed");
                }
                tagsA = new String[tags.size()];
                for (int i = 0; i< tags.size();i++){
                    tagsA[i] = tags.get(i);
                }
            }

        });

        tabBtn = findViewById(R.id.tab);

        tabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popMenu=new PopupMenu(view.getContext(),view);
                popMenu.setGravity(Gravity.END);
                popMenu.getMenu().add("manage tag 1").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        showDialog1();
                        return false;
                    }
                });
                popMenu.getMenu().add("manage tag 2").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        showDialog2();
                        return false;
                    }
                });
                popMenu.getMenu().add("manage tag3").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        showDialog3();
                        return false;
                    }
                });
                popMenu.show();
            }
        });
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.view_pager2);
        FragmentManager fm = getSupportFragmentManager();
        FragmentAdapter fragmentAdapter = new FragmentAdapter(fm, getLifecycle(),tagSelected1,tagSelected2,tagSelected3);
        viewPager2.setAdapter(fragmentAdapter);

        tabLayout.addTab(tabLayout.newTab().setText(tagSelected1));
        tabLayout.addTab(tabLayout.newTab().setText(tagSelected2));
        tabLayout.addTab(tabLayout.newTab().setText(tagSelected3));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    private void logout() {
        // sign out
        FirebaseAuth.getInstance().signOut();
        // send back to log in
        Intent intent = new Intent(MainActivity.this, SignIn.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mView.setBackgroundColor(getColor(SP.getInstance(this).getInt("theme", R.color.white)));
    }
    public void showDialog1(){
        tagSelected1 = tags.get(0);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your most favorite tag");
        builder.setSingleChoiceItems(tagsA, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tagSelected1=tagsA[i];
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               // tv.setText ( tagSelected1+"selected");
                tabLayout.removeTabAt(0);
                tabLayout.addTab(tabLayout.newTab().setText(tagSelected1),0);
                FragmentManager fm = getSupportFragmentManager();
                FragmentAdapter fragmentAdapter = new FragmentAdapter(fm, getLifecycle(),tagSelected1,tagSelected2,tagSelected3);
                viewPager2.setAdapter(fragmentAdapter);
            }
        });

        builder.setNegativeButton("Cancle",null);
        builder.show();}


    public void showDialog2(){
        tagSelected2 =tags.get(0);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your most favorite tag");
        builder.setSingleChoiceItems(tagsA, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tagSelected2=tagsA[i];
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               // tv.setText ( tagSelected2+"selected");
                tabLayout.removeTabAt(1);
                tabLayout.addTab(tabLayout.newTab().setText(tagSelected2),1);
                FragmentManager fm = getSupportFragmentManager();
                FragmentAdapter fragmentAdapter = new FragmentAdapter(fm, getLifecycle(),tagSelected1,tagSelected2,tagSelected3);
                viewPager2.setAdapter(fragmentAdapter);
            }
        });

        builder.setNegativeButton("Cancle",null);
        builder.show();}

    public void showDialog3(){
        tagSelected3 =tags.get(0);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your most favorite tag");
        builder.setSingleChoiceItems(tagsA, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tagSelected3=tagsA[i];
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
             //   tv.setText ( tagSelected3+"selected");
                tabLayout.removeTabAt(2);
                tabLayout.addTab(tabLayout.newTab().setText(tagSelected3),2);
                FragmentManager fm = getSupportFragmentManager();
                FragmentAdapter fragmentAdapter = new FragmentAdapter(fm, getLifecycle(),tagSelected1,tagSelected2,tagSelected3);
                viewPager2.setAdapter(fragmentAdapter);
            }
        });

        builder.setNegativeButton("Cancle",null);
        builder.show();}

}