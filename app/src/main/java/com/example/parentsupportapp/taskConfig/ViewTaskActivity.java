package com.example.parentsupportapp.taskConfig;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.parentsupportapp.R;
import com.example.parentsupportapp.childConfig.ViewActivity;
import com.example.parentsupportapp.model.TaskManager;

public class ViewTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        Toolbar toolbar = findViewById(R.id.tbView);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TaskManager temp = TaskManager.getInstance(this);
        ListView listView = findViewById(R.id.listTask);
        listView.setAdapter(new ArrayAdapter<String>(ViewTaskActivity.this, android.R.layout.simple_list_item_1, temp.getTaskAsString()));
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ViewActivity.class);
    }

}
