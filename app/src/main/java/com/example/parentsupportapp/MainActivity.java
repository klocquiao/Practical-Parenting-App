package com.example.parentsupportapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parentsupportapp.databinding.ActivityMainBinding;
import com.example.parentsupportapp.model.HistoryEntry;
import com.example.parentsupportapp.model.HistoryManager;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private HistoryManager historyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        historyManager = new HistoryManager(this);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryEntry testEntry = new HistoryEntry("name", 0, 0);
                historyManager.addCoinFlipEntry(testEntry);
                HistoryActivity.saveHistory(MainActivity.this, historyManager);
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = HistoryActivity.getIntent(MainActivity.this);
                startActivity(intent);
            }
        });

    }


}