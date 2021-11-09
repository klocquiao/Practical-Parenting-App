package com.example.parentsupportapp.childConfig;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.parentsupportapp.R;
import com.example.parentsupportapp.model.Family;

public class ViewActivity extends AppCompatActivity {

    private ListView listView;
    private Family fam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Toolbar toolbar = findViewById(R.id.tbView);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        fam = Family.getInstance(this);
        listView = findViewById(R.id.listChildren);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ViewActivity.this, android.R.layout.simple_list_item_1,fam.getChildrenInString());
        listView.setAdapter(arrayAdapter);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ViewActivity.class);
    }


}