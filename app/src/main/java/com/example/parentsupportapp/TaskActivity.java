package com.example.parentsupportapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class TaskActivity extends AppCompatActivity {

    private Button buttonAddTask;
    private Button buttonRemoveTask;
    private Button buttonEditTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        Toolbar toolbar = findViewById(R.id.toolBarTask);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        this.buttonAddTask = findViewById(R.id.buttonAddTask);
        this.buttonRemoveTask = findViewById(R.id.buttonRemoveTask);
        this.buttonEditTask = findViewById(R.id.buttonEditTask);

        setUpButtonListeners();
    }

    private void setUpButtonListeners() {

        this.buttonAddTask.setOnClickListener(v -> {
            Intent addTaskIntent = ChildConfigActivity.makeIntent(TaskActivity.this);
            startActivity(addTaskIntent);
        });

        this.buttonRemoveTask.setOnClickListener(v -> {
            Intent removeTaskIntent = ChildConfigActivity.makeIntent(TaskActivity.this);
            startActivity(removeTaskIntent);
        });

        this.buttonEditTask.setOnClickListener(v -> {
            Intent editTaskIntent = ChildConfigActivity.makeIntent(TaskActivity.this);
            startActivity(editTaskIntent);
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TaskActivity.class);
    }

}
