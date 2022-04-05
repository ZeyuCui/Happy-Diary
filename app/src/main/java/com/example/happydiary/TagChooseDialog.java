package com.example.happydiary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.example.happydiaryy.R;

import java.util.ArrayList;

public class TagChooseDialog extends AppCompatDialogFragment {
    AutoCompleteTextView autoCompleteTextView;
    ArrayList<String> tags = new ArrayList<String>();
    ArrayAdapter<String> adapterTags;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);
        adapterTags= new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.tag_item,tags);
        autoCompleteTextView= view.findViewById(R.id.auto_complete);
        autoCompleteTextView.setAdapter(adapterTags);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();

            }


        });
        builder.setView(view).setTitle("Select Tab");
        return super.onCreateDialog(savedInstanceState);
    }
}
