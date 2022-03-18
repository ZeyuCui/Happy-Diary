package com.example.happydiary.Zeyu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.happydiaryy.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class CreateActivity extends AppCompatActivity{

    TextView mytitle;
    TextView myLocText;
    EditText mycontent;
    FloatingActionButton savediary;
    ImageView myLocButton;
    String loc;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    ProgressBar myprog;

    private FusedLocationProviderClient fusedLocationClient;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        savediary =findViewById(R.id.savenote);
        mycontent =findViewById(R.id.createContent);
        mytitle =findViewById(R.id.createTitle);
        myprog =findViewById(R.id.progressbar);
        myLocText =findViewById(R.id.createLocation);
        myLocButton = findViewById(R.id.buttonLocation);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        Toolbar toolbar=findViewById(R.id.toolbarCreate);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Date date=new Date();
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd MMM yyyy");
        String sdata=dateFormat.format(date);
        mytitle.setText(sdata);


        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        loc="unknown";



        //get location
        myLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                locationEnabled();
                getLocation();
                //myLocText.setText(defaultLoc);

            }
        });





        savediary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title= mytitle.getText().toString();
                String content= mycontent.getText().toString();
                if(title.isEmpty() || content.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Both field are Require",Toast.LENGTH_SHORT).show();
                }
                else {
                    myprog.setVisibility(View.VISIBLE);
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("Full").document();
                    Map<String, Object> diary = new HashMap<>();
                    diary.put("title", title);
                    diary.put("location", loc);
                    diary.put("content", content);
                    diary.put("userid", firebaseUser.getUid());
                    documentReference.set(diary).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Note Created Succesffuly", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreateActivity.this, ListActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed To Create Note", Toast.LENGTH_SHORT).show();
                            myprog.setVisibility(View.INVISIBLE);
                            // startActivity(new Intent(createnote.this,notesactivity.class));
                        }
                    });

                    if (content.contains("<") && content.contains(">")){
                        String[] sKey = StringUtils.substringsBetween(content, "<", ">");
                        String[] sValue = StringUtils.substringsBetween(content, ">", "<");
                        String last = StringUtils.substringAfterLast(content, ">");
                        for (int i = 0; i < sKey.length; i++) {
                        documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection(sKey[i]).document();
                        DocumentReference referenceTag= firebaseFirestore.collection("tags").document(firebaseUser.getUid()).collection("tag").document();
                        Map<String, Object> tag = new HashMap<>();
                        tag.put("tagvalue",sKey[i]);
                        referenceTag.set(tag);
                        diary = new HashMap<>();
                        diary.put("title", title);
                        diary.put("location", loc);
                        diary.put("userid", firebaseUser.getUid());
                        if (i < sKey.length - 1)
                            diary.put("content", sValue[i]);
                        else
                            diary.put("content", last);
                        documentReference.set(diary).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Toast.makeText(getApplicationContext(),"Note Created Succesffuly",Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(createnote.this,notesactivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //  Toast.makeText(getApplicationContext(),"Failed To Create Note",Toast.LENGTH_SHORT).show();
                                myprog.setVisibility(View.INVISIBLE);
                                // startActivity(new Intent(createnote.this,notesactivity.class));
                            }
                        });

                    }
                }




                }
            }
        });






    }

    private void locationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(CreateActivity.this)
                    .setTitle("Enable GPS Service")
                    .setMessage("We need your GPS location to show Near Places around you.")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    void getLocation() {
        if (ContextCompat.checkSelfPermission(CreateActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(CreateActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(this)
                        .setTitle("Required Location Permission")
                        .setMessage("You have to give this permission to acess this feature")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(CreateActivity.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(CreateActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                Double latittude = location.getLatitude();
                                Double longitude = location.getLongitude();
                                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(latittude,longitude, 1);
                                    String cuLocation = addresses.get(0).getLocality()+" "+addresses.get(0).getPostalCode();
                                    myLocText.setText(cuLocation);
                                    loc=cuLocation;

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }



                            }
                        }
                    });

        }


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


}