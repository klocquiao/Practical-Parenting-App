package com.example.parentsupportapp.taskConfig;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.parentsupportapp.R;
import com.example.parentsupportapp.model.TaskManager;

public class EditTaskActivity extends AppCompatActivity {

    private TaskManager temp;
    private ListView listView;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        temp = TaskManager.getInstance(this);

        updateUI();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            View dialogView = getLayoutInflater().inflate(R.layout.edit_message_layout, null);
            EditText text = (EditText) dialogView.findViewById(R.id.editTextNewName);
            AlertDialog.Builder alert = new AlertDialog.Builder(EditTaskActivity.this);
            alert.setTitle("Edit Task").setView(dialogView).setPositiveButton(android.R.string.ok, (dialog, which) -> {
                String string = text.getText().toString();
                boolean flag = true;
                if (string.equals("")) {
                    text.setHint("Please Enter again! Input should not be empty!");
                    flag = false;
                }
                if (flag) {
                    temp.editTask(position, string);
                    updateUI();
                } else {
                    Toast.makeText(EditTaskActivity.this, "No Task entered", Toast.LENGTH_SHORT).show();
                }
            })
                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                        //empty
                    });
            alert.show();
            updateUI();
        });
    }

    private void updateUI() {
        temp = TaskManager.getInstance(EditTaskActivity.this);
        listView = findViewById(R.id.listViewTask);
        listView.setAdapter(new ArrayAdapter<>(EditTaskActivity.this, android.R.layout.simple_list_item_1, temp.getTaskAsString()));
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, EditTaskActivity.class);
    }

}
