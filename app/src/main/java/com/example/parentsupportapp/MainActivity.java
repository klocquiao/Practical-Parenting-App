package com.example.parentsupportapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parentsupportapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private Button configureChildButton;
    private Button flipButton;
    private Button timerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.parentsupportapp.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        this.configureChildButton = findViewById(R.id.menuConfigureChild);
        this.flipButton = findViewById(R.id.menuFlip);
        this.timerButton = findViewById(R.id.menuTimer);

        this.setupButtonListeners();
    }

    private void setupButtonListeners() {
        this.configureChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent childIntent = ChildConfigActivity.makeIntent(MainActivity.this);
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
                timerIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK); //might remove
                startActivity(timerIntent);
            }
        });
    }

}