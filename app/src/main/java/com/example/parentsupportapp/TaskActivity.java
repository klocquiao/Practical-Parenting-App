package com.example.parentsupportapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.parentsupportapp.model.TaskManager;
import com.example.parentsupportapp.taskConfig.AddTaskActivity;
import com.example.parentsupportapp.taskConfig.EditTaskActivity;
import com.example.parentsupportapp.taskConfig.RemoveTaskActivity;
import com.example.parentsupportapp.taskConfig.ViewTaskActivity;
import com.google.gson.Gson;


public class TaskActivity extends AppCompatActivity {

    private Button buttonAddTask;
    private Button buttonRemoveTask;
    private Button buttonEditTask;
    private Button buttonViewTask;
    public static final String EMPTY_PREF = "";
    private static final String KEY_Task = "TaskKey";
    private static final String PREF_TASK = "TaskPref";

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
            Intent viewTaskIntent = ViewTaskActivity.makeIntent(TaskActivity.this);
            startActivity(viewTaskIntent);
        });

        this.buttonAddTask.setOnClickListener(v -> {
            Intent addTaskIntent = AddTaskActivity.makeIntent(TaskActivity.this);
            startActivity(addTaskIntent);
        });

        this.buttonRemoveTask.setOnClickListener(v -> {
            Intent removeTaskIntent = RemoveTaskActivity.makeIntent(TaskActivity.this);
            startActivity(removeTaskIntent);
        });

        this.buttonEditTask.setOnClickListener(v -> {
            Intent editTaskIntent = EditTaskActivity.makeIntent(TaskActivity.this);
            startActivity(editTaskIntent);
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TaskActivity.class);
    }

    public static void saveTaskSharedPrefs(Context context, TaskManager temp) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_TASK, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String jsonTask = gson.toJson(temp.getTaskArray());
        editor.putString(KEY_Task, jsonTask);
        editor.apply();
    }

    public static String getTask(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_TASK, MODE_PRIVATE);
        return pref.getString(KEY_Task, EMPTY_PREF);
    }

}
