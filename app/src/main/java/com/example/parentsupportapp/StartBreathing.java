package com.example.parentsupportapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StartBreathing extends AppCompatActivity {
    private Button startButton;
    private EditText etNumBreaths;
    private Button btnIncrease;
    private Button btnDecrease;
    public int numBreaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_breathing);

        etNumBreaths = findViewById(R.id.etNumBreaths);
        btnIncrease = findViewById(R.id.btnAddBreath);
        btnDecrease = findViewById(R.id.btnDecreaseBreath);
        numBreaths = Integer.parseInt(etNumBreaths.getText().toString());

        setupButtonListeners();
        setupIncreaseBreathBtn();
        setupDecreaseBreathBtn();
    }

    private void setupDecreaseBreathBtn() {
        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numBreaths > 1) {
                    numBreaths--;
                    etNumBreaths.setText("" + numBreaths);
                }
                else if (numBreaths == 1) {
                    Toast.makeText(StartBreathing.this, "Number of breaths cannot be less than 1!", Toast.LENGTH_SHORT).show();
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
                    etNumBreaths.setText("" + numBreaths);
                }
                else if (numBreaths == 10) {
                    Toast.makeText(StartBreathing.this, "Number of breaths cannot be more than 10!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupButtonListeners() {
        startButton = findViewById(R.id.btnStartBreath);
        this.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent breathingIntent = BreathingActivity.makeIntent(StartBreathing.this,
                        Integer.parseInt(etNumBreaths.getText().toString()));
                startActivity(breathingIntent);
            }
        });
    }

    public static Intent makeIntent(Context c) {
        Intent intent = new Intent(c, StartBreathing.class);
        return intent;
    }

}