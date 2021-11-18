package com.example.parentsupportapp.tasksConfig;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parentsupportapp.R;

public class EditTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, EditTaskActivity.class);
    }
}