package com.example.parentsupportapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class TimerActivity extends AppCompatActivity {

    private static final long DEFAULT_START_TIME = 300000;
    private static final long ONE_MIN = 60000;
    private static final long TWO_MIN = 120000;
    private static final long THREE_MIN = 180000;
    private static final long FIVE_MIN = 300000;
    private static final long TEN_MIN = 600000;

    private TextView timerView;
    private Button startButton;
    private Button resetButton;
    private Button customMinButton;
    private Button oneMinButton;
    private Button twoMinButton;
    private Button threeMinButton;
    private Button fiveMinButton;
    private Button tenMinButton;
    private CountDownTimer countDownTimer;
    private Vibrator vibrator;
    private NotificationAssistant notificationAssistant;
    private NotificationCompat.Builder builder;
    private NotificationManagerCompat manager;
    private boolean isTicking;
    private long timeLeftInMill = DEFAULT_START_TIME;
    private long lastSelectedTime = DEFAULT_START_TIME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        initializeButtons();
        setupButtonListeners();
        setupVibrator();
        setupNotificationEnvironment();
        updateTimerTextView();
    }


    private void initializeButtons() {
        timerView = (TextView) findViewById(R.id.textViewTimer);
        startButton = (Button) findViewById(R.id.buttonStart);
        resetButton = (Button) findViewById(R.id.buttonStop);
        customMinButton = (Button) findViewById(R.id.buttonCustomMin);
        oneMinButton = (Button) findViewById(R.id.buttonOneMin);
        twoMinButton = (Button) findViewById(R.id.buttonTwoMin);
        threeMinButton = (Button) findViewById(R.id.buttonThreeMin);
        fiveMinButton = (Button) findViewById(R.id.buttonFiveMin);
        tenMinButton = (Button) findViewById(R.id.buttonTenMin);
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

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerReset();
            }
        });

        customMinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForCustomTime();
            }
        });

        setOnClickForMinButton(oneMinButton, ONE_MIN);
        setOnClickForMinButton(twoMinButton, TWO_MIN);
        setOnClickForMinButton(threeMinButton, THREE_MIN);
        setOnClickForMinButton(fiveMinButton, FIVE_MIN);
        setOnClickForMinButton(tenMinButton, TEN_MIN);
    }

    private void setupVibrator() {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    private void setupNotificationEnvironment() {
        notificationAssistant = new NotificationAssistant(this);
        notificationAssistant.createNotificationChannel();
        builder = notificationAssistant.createBuilder();
        manager = notificationAssistant.createManager();
    }

    private void askForCustomTime() {
        View dialogView = getLayoutInflater().inflate(R.layout.timer_alert_layout, null);

        EditText minuteText = (EditText)dialogView.findViewById(R.id.editTextTimerAlertMinute);
        EditText secondText = (EditText)dialogView.findViewById(R.id.editTextTimerAlertSecond);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Provide time");
        alert.setView(dialogView);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // TODO: Have to check returned value and have to adjust create custom view to

                Boolean shouldStart = true;
                String minuteString = minuteText.getText().toString();
                String secondString = secondText.getText().toString();
                long extractedMinutes;
                long extractedSeconds;
                try {
                    extractedMinutes = Long.parseLong(minuteString);
                    extractedSeconds = Long.parseLong(secondString);
                    timeLeftInMill = (extractedMinutes * 60 * 1000) + (extractedSeconds * 1000);
                    lastSelectedTime = timeLeftInMill;
                }
                catch (NumberFormatException e) {
                    // TODO: delete this toast when remove seconds field
                    Toast.makeText(TimerActivity.this, "Please provide a valid time :)", Toast.LENGTH_SHORT).show();
                    shouldStart = false;
                }
                if (!isTicking && shouldStart) {
                    startTimer();
                }
                updateTimerTextView();
            }
        });
        alert.show();
    }

    private void timerReset() {
        if (isTicking) {
            Toast.makeText(TimerActivity.this, "Can't reset while running!", Toast.LENGTH_SHORT).show();
        } else {
            timeLeftInMill = lastSelectedTime;
            updateTimerTextView();
            updateUIShowButtons();
            startButton.setText(R.string.timerActivity_start);
        }
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        isTicking = false;
        startButton.setText(R.string.timerActivity_start);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMill, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMill = l;
                updateTimerTextView();
            }

            @RequiresApi(api = Build.VERSION_CODES.S)
            @Override
            public void onFinish() {
                Toast.makeText(TimerActivity.this, "TIMER COMPLETE!", Toast.LENGTH_SHORT).show();
                pauseTimer();
                timerReset();
                vibrateEndOfTimer();
                sendNotification();
            }
        }.start();

        startButton.setText(R.string.timerActivity_pause);
        isTicking = true;
        updateUIHideButtons();
    }

    private void vibrateEndOfTimer() {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            Log.i("Vibrator Issue", "The vibrator initialized to null.");
        }
    }

    private void sendNotification() {
        manager.notify(1, builder.build());
    }

    private void updateUIHideButtons() {
        buttonDisappear(oneMinButton);
        buttonDisappear(twoMinButton);
        buttonDisappear(threeMinButton);
        buttonDisappear(fiveMinButton);
        buttonDisappear(tenMinButton);
        buttonDisappear(customMinButton);
        Guideline guideline = (Guideline) findViewById(R.id.guidelineHorizontal);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideline.getLayoutParams();
        params.guidePercent = (float) 0.7;
        guideline.setLayoutParams(params);
    }

    private void updateUIShowButtons() {
        Guideline guideline = (Guideline) findViewById(R.id.guidelineHorizontal);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideline.getLayoutParams();
        params.guidePercent = (float) 0.4;
        guideline.setLayoutParams(params);
        buttonAppear(oneMinButton);
        buttonAppear(twoMinButton);
        buttonAppear(threeMinButton);
        buttonAppear(fiveMinButton);
        buttonAppear(tenMinButton);
        buttonAppear(customMinButton);
    }

    private void buttonAppear(Button button) {
        button.setClickable(true);
        button.setVisibility(View.VISIBLE);
    }

    private void buttonDisappear(Button button) {
        button.setClickable(false);
        button.setVisibility(View.GONE);
    }

    private void setOnClickForMinButton(Button btn, long time) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // safety check to make sure it doesn't start new timer while one is already ticking
                if (!isTicking) {
                    timeLeftInMill = time;
                    lastSelectedTime = time;
                    updateTimerTextView();
                    startTimer();
                }
            }
        });
    }

    private void updateTimerTextView() {
        int seconds = (int) timeLeftInMill / 1000 % 60;
        int minutes = (int) timeLeftInMill / 1000 / 60;
        String timeLeft = String.format(Locale.CANADA, "%02d:%02d", minutes, seconds);
        timerView.setText(timeLeft);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TimerActivity.class);
    }
}