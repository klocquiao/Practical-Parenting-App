package com.example.parentsupportapp.tasksConfig;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.parentsupportapp.R;
import com.example.parentsupportapp.model.Family;
import com.example.parentsupportapp.model.Task;
import com.example.parentsupportapp.model.TaskManager;

import java.util.ArrayList;

public class RemoveTaskActivity extends AppCompatActivity {

    private TaskManager taskManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_task);
        Toolbar toolbar = findViewById(R.id.toolbarRemoveTask);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle(R.string.remove_task_activity_title);
        }

        initializeVarialbes();
        updateUI();
        registerClickCallback();
    }

    private void initializeVarialbes() {
        Family family = Family.getInstance(RemoveTaskActivity.this);
        taskManager = TaskManager.getInstance(family.getChildren(), RemoveTaskActivity.this);
    }

    private void updateUI() {
        ArrayList<Task> tasks = taskManager.getTaskArray();

        ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(this,
                R.layout.basic_list_item_view,
                R.id.textViewBasicListItem,
                tasks);

        ListView list = findViewById(R.id.listViewRemoveTask);
        list.setAdapter(adapter);
    }

    private void registerClickCallback() {
        ListView list = findViewById(R.id.listViewRemoveTask);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                confirmationPopup(position);
            }
        });
    }

    private void confirmationPopup(int position) {
        View dialogView = getLayoutInflater().inflate(R.layout.remove_task_message_layout, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(RemoveTaskActivity.this);
        alert.setTitle(R.string.remove_task_removal_message)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        taskManager.removeTask(position);
                        updateUI();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { }
                });
        alert.show();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, RemoveTaskActivity.class);
    }
}