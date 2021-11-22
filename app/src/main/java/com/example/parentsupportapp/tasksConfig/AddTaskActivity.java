package com.example.parentsupportapp.tasksConfig;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.parentsupportapp.R;
import com.example.parentsupportapp.model.Family;
import com.example.parentsupportapp.model.TaskManager;

public class AddTaskActivity extends AppCompatActivity {
    private TaskManager taskManager;
    private Family family;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = findViewById(R.id.toolbarAddTask);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Add Task");
        }

        family = Family.getInstance(this);
        taskManager = TaskManager.getInstance(family.getChildren(), this);

        setupButton();
    }

    private void setupButton() {
        Button button = findViewById(R.id.buttonConfirmAddTask);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = findViewById(R.id.editTextTaskName);
                String extractedText = editText.getText().toString();
                if (extractedText.equals("")) {
                    Toast.makeText(AddTaskActivity.this, "Please provide a task name :) !", Toast.LENGTH_SHORT).show();
                } else {
                    taskManager.addTask(family.getChildren(), extractedText);
                    Toast.makeText(AddTaskActivity.this, "New Task Added!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, AddTaskActivity.class);
    }
}