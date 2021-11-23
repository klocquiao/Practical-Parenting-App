package com.example.parentsupportapp.tasksConfig;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.parentsupportapp.R;
import com.example.parentsupportapp.TasksActivity;
import com.example.parentsupportapp.model.Family;
import com.example.parentsupportapp.model.Task;
import com.example.parentsupportapp.model.TaskManager;

import java.util.ArrayList;

public class EditTaskActivity extends AppCompatActivity {

    private Family family;
    private TaskManager taskManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Toolbar toolbar = findViewById(R.id.toolbarEditTask);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle(R.string.edit_task_title);
        }

        initializeVariables();
        updateUI();
        registerClickCallback();
    }

    private void initializeVariables() {
        family = Family.getInstance(EditTaskActivity.this);
        taskManager = TaskManager.getInstance(family.getChildren(), EditTaskActivity.this);
    }

    private void updateUI() {
        ArrayList<Task> tasks = taskManager.getTaskArray();

        ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(this,
                R.layout.basic_list_item_view,
                R.id.textViewBasicListItem,
                tasks);

        ListView list = findViewById(R.id.listViewEditTask);
        list.setAdapter(adapter);
    }

    private void registerClickCallback() {
        ListView list = findViewById(R.id.listViewEditTask);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                editTaskName(position);
            }
        });
    }

    private void editTaskName(int position) {
        View dialogView = getLayoutInflater().inflate(R.layout.edit_message_layout, null);
        EditText editText = dialogView.findViewById(R.id.editTextNewName);
        AlertDialog.Builder alert = new AlertDialog.Builder(EditTaskActivity.this);
        alert.setTitle(R.string.edit_task_alert_title)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newName = editText.getText().toString();

                        if (newName.equals("")) {
                            Toast.makeText(EditTaskActivity.this,
                                    getString(R.string.edit_task_alert_message),
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                        else {
                            taskManager.getTask(position).setTaskName(newName);
                            TasksActivity.saveTaskSharedPrefs(EditTaskActivity.this, taskManager);
                            updateUI();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { }
                });
        alert.show();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, EditTaskActivity.class);
    }
}