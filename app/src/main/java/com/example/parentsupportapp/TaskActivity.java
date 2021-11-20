package com.example.parentsupportapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.parentsupportapp.tasksConfig.AddTaskActivity;
import com.example.parentsupportapp.tasksConfig.EditTaskActivity;
import com.example.parentsupportapp.tasksConfig.RemoveTaskActivity;
//import com.example.parentsupportapp.tasksConfig.ViewTaskActivity;


public class TaskActivity extends AppCompatActivity {

    private Button buttonAddTask;
    private Button buttonRemoveTask;
    private Button buttonEditTask;
    private Button buttonViewTask;

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
        this.buttonViewTask = findViewById(R.id.buttonViewTask);

        setUpButtonListeners();
    }

    private void setUpButtonListeners() {

        this.buttonViewTask.setOnClickListener(v -> {
            //startActivity(ViewTaskActivity.makeIntent(TaskActivity.this));
        });

        this.buttonAddTask.setOnClickListener(v -> {
            startActivity(AddTaskActivity.makeIntent(TaskActivity.this));
        });

        this.buttonRemoveTask.setOnClickListener(v -> {
            startActivity(RemoveTaskActivity.makeIntent(TaskActivity.this));
        });

        this.buttonEditTask.setOnClickListener(v -> {
            startActivity(EditTaskActivity.makeIntent(TaskActivity.this));
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TaskActivity.class);
    }
}
