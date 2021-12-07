package com.example.parentsupportapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parentsupportapp.model.PriorityQueue;
import com.google.gson.Gson;

/**
 * StartBreathingActivity is the start of the state model of the breathing exercise feature.
 * It allows the user to set the number of breaths they would like to perform in the exercise.
 * The "Begin" button brings the user to the breathing activity whether the true breathing exercise will begin.
 */

public class StartBreathingActivity extends AppCompatActivity {
    private static final String BREATHING_PREF = "BreathingActivityPref";
    private static final String BREATHING_KEY = "BreathingActivityKey";
    private Button startButton;
    private EditText etNumBreaths;
    private Button btnIncrease;
    private Button btnDecrease;
    public int numBreaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_breathing);
        Toolbar toolbar = findViewById(R.id.toolbarStartBreathing);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        numBreaths = getBreathingActivityPrefs(this);
        etNumBreaths = findViewById(R.id.etNumBreaths);
        etNumBreaths.setText(Integer.toString(numBreaths));

        btnIncrease = findViewById(R.id.btnAddBreath);
        btnDecrease = findViewById(R.id.btnDecreaseBreath);

        setupButtonListeners();
        setupIncreaseBreathBtn();
        setupDecreaseBreathBtn();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveBreathingActivityPrefs(this);
    }

    private void setupDecreaseBreathBtn() {
        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numBreaths > 1) {
                    numBreaths--;
                    etNumBreaths.setText(Integer.toString(numBreaths));
                }
                else if (numBreaths == 1) {
                    Toast.makeText(StartBreathingActivity.this, "Number of breaths cannot be less than 1!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupIncreaseBreathBtn() {
        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numBreaths <= 9) {
                    numBreaths++;
                    etNumBreaths.setText(Integer.toString(numBreaths));
                }
                else if (numBreaths == 10) {
                    Toast.makeText(StartBreathingActivity.this, "Number of breaths cannot be more than 10!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupButtonListeners() {
        startButton = findViewById(R.id.btnStartBreath);
        this.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent breathingIntent = BreathingActivity.makeIntent(StartBreathingActivity.this,
                        Integer.parseInt(etNumBreaths.getText().toString()));
                startActivity(breathingIntent);
            }
        });
    }

    public static Integer getBreathingActivityPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(BREATHING_PREF, MODE_PRIVATE);
        return prefs.getInt(BREATHING_KEY, 1);
    }

    private void saveBreathingActivityPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(BREATHING_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(BREATHING_KEY, numBreaths);
        editor.apply();
    }

    public static Intent makeIntent(Context c) {
        Intent intent = new Intent(c, StartBreathingActivity.class);
        return intent;
    }
}