package com.example.parentsupportapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.parentsupportapp.childConfig.AddChildActivity;
import com.example.parentsupportapp.childConfig.RemoveActivity;
import com.example.parentsupportapp.childConfig.ViewActivity;

public class ChildConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_config);

        setupAddButton();
        setupRemoveButton();
        //setupEditButton();
        setupViewButton();
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
    /*
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

     */

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
}