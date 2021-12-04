package com.example.parentsupportapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parentsupportapp.childConfig.ViewActivity;
import com.example.parentsupportapp.databinding.ActivityMainBinding;

/**
 * The MainActivity class handles the model for the main/initial menu
 * It consists of three buttons for three different activities
 * The setupButtonListeners method is set so that on click of each of the three
 * buttons it will shift to the respective activities.
 */

public class MainActivity extends AppCompatActivity {
    private Button configureChildButton;
    private Button flipButton;
    private Button timerButton;
    private Button tasksButton;
    private Button helpButton;
    private Button breathButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.parentsupportapp.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.main_menu_activity_title));

        this.configureChildButton = findViewById(R.id.buttonConfigChild);
        this.flipButton = findViewById(R.id.buttonFlipCoin);
        this.timerButton = findViewById(R.id.buttonTimer);
        this.tasksButton = findViewById(R.id.buttonTasks);
        this.helpButton = findViewById(R.id.buttonHelp);
        this.breathButton = findViewById(R.id.buttonBreathing);

        this.setupButtonListeners();
    }

    private void setupButtonListeners() {
        this.configureChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent childIntent = ViewActivity.makeIntent(MainActivity.this);
                startActivity(childIntent);
            }
        });

        this.flipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent flipIntent = CoinFlipActivity.makeIntent(MainActivity.this);
                startActivity(flipIntent);
            }
        });

        this.timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent timerIntent = TimerActivity.makeIntent(MainActivity.this);
                startActivity(timerIntent);
            }
        });

        this.tasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent taskIntent = TasksActivity.makeIntent(MainActivity.this);
                startActivity(taskIntent);
            }
        });

        this.helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent helpIntent = HelpActivity.makeIntent(MainActivity.this);
                startActivity(helpIntent);
            }
        });

        this.breathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent breathingIntent = StartBreathing.makeIntent(MainActivity.this);
                startActivity(breathingIntent);
            }
        });
    }
}