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
                startActivity(new Intent(MainActivity.this, CoinFlipActivity.class));
            }
        });

        this.timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent timerIntent = TimerActivity.makeIntent(MainActivity.this);
                startActivity(timerIntent);
            }
        });
    }

}