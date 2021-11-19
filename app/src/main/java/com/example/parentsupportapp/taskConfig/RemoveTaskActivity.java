package com.example.parentsupportapp.taskConfig;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.parentsupportapp.R;
import com.example.parentsupportapp.model.TaskManager;

public class RemoveTaskActivity extends AppCompatActivity {

    private ListView listView;
    private TaskManager temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_task);

        Toolbar toolbar = findViewById(R.id.tbRem);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        temp = TaskManager.getInstance(this);
        updateUI();
        listView.setOnItemClickListener((parent, view, position, id) -> {
            View dialogView = getLayoutInflater().inflate(R.layout.remove_message_layout, null);
            AlertDialog.Builder alert = new AlertDialog.Builder(RemoveTaskActivity.this);
            alert.setTitle("Confirm").setView(dialogView).setPositiveButton(android.R.string.ok, (dialog, which) -> {
                temp.removeTask(position);
                updateUI();
            }).setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                //empty
            });
            alert.show();
            updateUI();
        });
    }

    private void updateUI() {
        listView = findViewById(R.id.viewTaskRemove);
        listView.setAdapter(new ArrayAdapter<>(RemoveTaskActivity.this, android.R.layout.simple_list_item_1, temp.getTaskAsString()));
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, RemoveTaskActivity.class);
    }

}
