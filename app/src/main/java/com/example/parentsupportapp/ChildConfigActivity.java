package com.example.parentsupportapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.parentsupportapp.childConfig.AddChildActivity;
import com.example.parentsupportapp.childConfig.EditActivity;
import com.example.parentsupportapp.childConfig.RemoveActivity;
import com.example.parentsupportapp.childConfig.ViewActivity;
import com.example.parentsupportapp.model.Family;
import com.example.parentsupportapp.model.HistoryManager;
import com.google.gson.Gson;

public class ChildConfigActivity extends AppCompatActivity {
    public static final String EMPTY_PREF = "";
    private static final String KEY_FAMILY = "FamilyKey";
    private static final String PREF_CHILD_CONFIG = "ChildConfigPref";
    private Family family;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_config);

        Toolbar toolbar = findViewById(R.id.tbAddChild);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        setupAddButton();
        setupRemoveButton();
        setupEditButton();
        setupViewButton();

        family = Family.getInstance(this);
    }

    private void setupAddButton(){
        Button btnAdd = findViewById(R.id.btnAddChild);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddChildActivity.makeIntent(ChildConfigActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setupRemoveButton() {
        Button btn = findViewById(R.id.btnRemove);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = RemoveActivity.makeIntent(ChildConfigActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setupEditButton() {
        Button btn = findViewById(R.id.btnEdit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = EditActivity.makeIntent(ChildConfigActivity.this);
                startActivity(intent);
            }
        });
    }



    public static void saveChildConfigPrefs(Context context, Family family) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_CHILD_CONFIG, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String jsonFamily = gson.toJson(family.getChildren());
        editor.putString(KEY_FAMILY, jsonFamily);
        editor.apply();
    }

    public static String getFamily(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_CHILD_CONFIG, MODE_PRIVATE);
        return prefs.getString(KEY_FAMILY, EMPTY_PREF);
    }

    private void setupViewButton() {
        Button btn = findViewById(R.id.btnView);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ViewActivity.makeIntent(ChildConfigActivity.this);
                startActivity(intent);
            }
        });
    }

    public static Intent makeIntent(Context c) {
        Intent intent = new Intent(c, ChildConfigActivity.class);
        return intent;
    }
}