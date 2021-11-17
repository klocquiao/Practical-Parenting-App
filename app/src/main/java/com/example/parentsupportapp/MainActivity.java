package com.example.parentsupportapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parentsupportapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    /**
     * The MainActivity class handles the model for the main/initial menu
     * It consists of three buttons for three different activities
     * The setupButtonListeners method is set so that on click of each of the three
     * buttons it will shift to the respective activities.
     */

    private Button configureChildButton;
    private Button flipButton;
    private Button timerButton;
    private Button taskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.parentsupportapp.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Parent Support App");
        }

        this.configureChildButton = findViewById(R.id.buttonConfigChild);
        this.flipButton = findViewById(R.id.buttonFlipCoin);
        this.timerButton = findViewById(R.id.buttonTimer);
        this.taskButton = findViewById(R.id.buttonTask);

        this.setupButtonListeners();
    }

    private void setupButtonListeners() {

        this.configureChildButton.setOnClickListener(v -> {
            Intent childIntent = ChildConfigActivity.makeIntent(MainActivity.this);
            startActivity(childIntent);
        });

        this.flipButton.setOnClickListener(v -> {
            Intent flipIntent = CoinFlipActivity.makeIntent(MainActivity.this);
            startActivity(flipIntent);
        });

        this.timerButton.setOnClickListener(v -> {
            Intent timerIntent = TimerActivity.makeIntent(MainActivity.this);
            startActivity(timerIntent);
        });

        this.taskButton.setOnClickListener(v -> {
            Intent taskIntent = TaskActivity.makeIntent(MainActivity.this);
            startActivity(taskIntent);
        });
    }

}