package com.example.parentsupportapp.taskConfig;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.parentsupportapp.R;
import com.example.parentsupportapp.model.TaskManager;

public class AddTaskActivity extends AppCompatActivity {

    private TaskManager temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Toolbar toolbar = findViewById(R.id.toolBarAddTask);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        temp = TaskManager.getInstance();
        setupAddButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void setupAddButton () {
        Button button = findViewById(R.id.buttonAddTask);
        button.setOnClickListener(v -> {
            EditText name = findViewById(R.id.enterName);
            String name2 = name.getText().toString();
            name.setHint("Enter a name for the task");
            if(!checkName(name2, name)) {
                return;
            }
            temp.addTask(name2);
            name.setText("");
            Toast.makeText(AddTaskActivity.this, "New Task Added", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean checkName (String name, EditText editText) {
        if (name.equals("")) {
            editText.setHint("Enter again!");
            return false;
        }
        return true;
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, AddTaskActivity.class);
    }

}
