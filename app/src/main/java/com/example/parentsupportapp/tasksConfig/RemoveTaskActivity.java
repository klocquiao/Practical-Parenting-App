package com.example.parentsupportapp.tasksConfig;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.parentsupportapp.R;

public class RemoveTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_task);
        Toolbar toolbar = findViewById(R.id.toolbarRemoveTask);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Remove Task");
        }

    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, RemoveTaskActivity.class);
    }
}