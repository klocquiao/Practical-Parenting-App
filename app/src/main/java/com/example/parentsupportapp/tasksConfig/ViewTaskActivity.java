package com.example.parentsupportapp.tasksConfig;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.parentsupportapp.HistoryActivity;
import com.example.parentsupportapp.R;
import com.example.parentsupportapp.TasksActivity;
import com.example.parentsupportapp.childConfig.ViewActivity;
import com.example.parentsupportapp.model.Family;
import com.example.parentsupportapp.model.Task;
import com.example.parentsupportapp.model.TaskManager;

/**
 * ViewTaskActivity contains the hub for the Task functionality of the application.
 * It contains 3 buttons that takes the user to add, edit, or remove tasks respectively.
 */

public class ViewTaskActivity extends AppCompatActivity {

    private Family family;
    private TaskManager taskManager;
    private Task currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        Toolbar toolbar = findViewById(R.id.toolbarViewTask);
        setSupportActionBar(toolbar);
        ActionBar actionBar =  getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.view_task_title);
        }
        
        initializeVariables();
        setupView();
        setupButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_flip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.actionHistory:
                Intent historyIntent = HistoryActivity.makeIntent(this, currentTask.getSerializedTaskHistory(), true);
                startActivity(historyIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeVariables() {
        family = Family.getInstance(this);
        taskManager = TaskManager.getInstance(family.getChildren(), this);
        extractTask();
    }

    private void extractTask() {
        Bundle bundle = getIntent().getExtras();
        int currentTaskPosition = bundle.getInt(TasksActivity.TASK_POSITION);
        currentTask = taskManager.getTask(currentTaskPosition);
    }

    private void setupView() {
        TextView childName = findViewById(R.id.textViewCurrentChildName);
        childName.setText(currentTask.getNextChildInQueueName());

        ImageView childImage = findViewById(R.id.imageViewCurrentChild);
        ViewActivity.loadImageFromStorage(currentTask.getNextChildInQueueImage(), childImage, ViewTaskActivity.this);

        TextView taskName = findViewById(R.id.textViewCurrentTaskName);
        taskName.setText(currentTask.getTaskName());
    }

    private void setupButton() {
        Button confirmButton = findViewById(R.id.buttonTaskConfirmChildTurn);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTask.moveFirstChildToBack();
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ViewTaskActivity.class);
    }
}