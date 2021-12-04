package com.example.parentsupportapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.parentsupportapp.childConfig.ViewActivity;

public class StartBreathing extends AppCompatActivity {
    private Button startButton;
    private EditText numBreaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_breathing);
        numBreaths = findViewById(R.id.etBreaths);
        setupButtonListeners();
    }

    private void setupButtonListeners() {
        startButton = findViewById(R.id.btnStartBreath);
        this.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent breathingIntent = BreathingActivity.makeIntent(StartBreathing.this,
                        Integer.parseInt(numBreaths.getText().toString()));
                startActivity(breathingIntent);
            }
        });
    }

    public static Intent makeIntent(Context c) {
        Intent intent = new Intent(c, StartBreathing.class);
        return intent;
    }

}