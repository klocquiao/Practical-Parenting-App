package com.example.parentsupportapp.taskConfig;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class EditTaskActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        return new Intent(context, EditTaskActivity.class);
    }

}
