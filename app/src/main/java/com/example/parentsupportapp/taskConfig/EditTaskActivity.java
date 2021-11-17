package com.example.parentsupportapp.taskConfig;

import android.app.AlertDialog;
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

        Toolbar toolbar = findViewById(R.id.tbEdit);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        temp = TaskManager.getInstance(this);

        updateUI();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View dialogView = getLayoutInflater().inflate(R.layout.edit_message_layout, null);
                EditText text = (EditText) dialogView.findViewById(R.id.editTextNewName);
                AlertDialog.Builder alert = new AlertDialog.Builder(EditTaskActivity.this);
                alert.setTitle("Edit Task").setView(dialogView).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
                    }
                })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //empty
                            }
                        });
                alert.show();
                updateUI();
            }
        });
    }

    private void updateUI() {
        temp = TaskManager.getInstance(EditTaskActivity.this);
        listView = findViewById(R.id.listTaskToEdit);
        listView.setAdapter(new ArrayAdapter<String>(EditTaskActivity.this, android.R.layout.simple_list_item_1, temp.getTaskAsString()));
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, EditTaskActivity.class);
    }

}
