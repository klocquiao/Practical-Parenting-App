package com.example.parentsupportapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TimerActivity extends AppCompatActivity {

    private static final long START_TIME = 300000;
    private static final long ONE_MIN = 60000;

    private CountDownTimer countDownTimer;
    private TextView timerView;
    private Button startButton;
    private Button stopButton;
    private Button oneMinButton;
    private Button twoMinButton;
    private Button threeMinButton;
    private Button fiveMinButton;
    private Button tenMinButton;
    private Button customMinButton;
    private boolean isTicking;
    private long timeLeftInMill = START_TIME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        timerView = (TextView) findViewById(R.id.textViewTimer);
        startButton = (Button) findViewById(R.id.buttonStart);
        stopButton = (Button) findViewById(R.id.buttonStop);
        oneMinButton = (Button) findViewById(R.id.buttonOneMin);
        customMinButton = (Button) findViewById(R.id.buttonCustomMin);

        setupButtonListeners();

        updateTimerTextView();
    }

    private void setupButtonListeners() {

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTicking) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerReset();
            }
        });

        oneMinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeLeftInMill = ONE_MIN;
                updateTimerTextView();
                startTimer();
            }
        });

        customMinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Insert dialog box that appears asking for custom time

                updateTimerTextView();
            }
        });
    }

    private void timerReset() {
        if (isTicking) {
            Toast.makeText(TimerActivity.this, "Can't reset while running!", Toast.LENGTH_SHORT).show();
        } else {
            timeLeftInMill = START_TIME;
            updateTimerTextView();
        }
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        isTicking = false;
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMill, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMill = l;
                updateTimerTextView();
            }

            @Override
            public void onFinish() {
                // TODO: this (possibly from thread handler) is where I need to alert through a notification
                Toast.makeText(TimerActivity.this, "TIMER COMPLETE!", Toast.LENGTH_SHORT).show();
            }
        }.start();
        
        isTicking = true;
    }

    private void updateTimerTextView() {
        // convert timeLeftInMill into seconds
        int seconds = (int) timeLeftInMill / 1000 % 60;

        // convert timeLeftInMill into minutes
        int minutes = (int) timeLeftInMill / 1000 / 60;

        String timeLeft = String.format("%02d:%02d", minutes, seconds);

        timerView.setText(timeLeft);
    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, TimerActivity.class);
    }
}