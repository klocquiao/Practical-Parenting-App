package com.example.parentsupportapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.parentsupportapp.model.Family;

public class TasksActivity extends AppCompatActivity {

    private Family fam;
    private Button addTaskButton;
    private Button editTaskButton;
    private Button removeTaskButton;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        Toolbar toolbar = findViewById(R.id.appBarLayoutTasks);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle(R.string.tasks_title);
        }

        initializeFields();
        setupButtonListeners();
    }

    private void initializeFields() {
        addTaskButton = findViewById(R.id.buttonAddTask);
        editTaskButton = findViewById(R.id.buttonEditTask);
        removeTaskButton = findViewById(R.id.buttonRemoveTask);
        fam = Family.getInstance(this);
        listView = findViewById(R.id.listViewTasks);
    }

    private void setupButtonListeners() {
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setOnClickBehaviour(Button button, Intent intent) {

    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TimerActivity.class);
    }
}