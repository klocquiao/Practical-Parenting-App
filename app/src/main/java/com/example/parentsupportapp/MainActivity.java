package com.example.parentsupportapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parentsupportapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        Button buttonTimer = (Button) findViewById(R.id.buttonMainTimer);
        buttonTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = TimerActivity.makeIntent(MainActivity.this);
                startActivity(i);
            }
        });

    }
}