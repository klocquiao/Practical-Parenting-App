package com.example.parentsupportapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
This activity handles the timer functionality for the app. The timer activity will continue
counting down from the selected time, whether preset or custom time. When the timer reaches 0,
the activity causes the phone to vibrate, starts an alarm, and sends a notification (top level)
to the user to allow them to silence the alarm or go back to the activity when the notification
is clicked.
 */

public class TimerActivity extends AppCompatActivity {
    private static final long DEFAULT_START_TIME = 300000;
    private static final long ONE_MIN = 60000;
    private static final long TWO_MIN = 120000;
    private static final long THREE_MIN = 180000;
    private static final long FIVE_MIN = 300000;
    private static final long TEN_MIN = 600000;
    private static final int NOTIFICATION_ID = 1;
    public static final int MILLISECONDS_TO_SECONDS = 1000;
    public static final int SECONDS_TO_MINUTES = 60;
    public static final float GUIDE_PERCENT_1 = (float) 0.7;
    public static final float GUIDE_PERCENT_2 = (float) 0.4;

    private PieChart pieChart;
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
    private static Ringtone ringtone;
    private boolean isTicking;
    private long timeLeftInMill = DEFAULT_START_TIME;
    private long lastSelectedTime = DEFAULT_START_TIME;
    private long timeDiffStartVsLeft = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        Toolbar toolbar = findViewById(R.id.toolbarTimer);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initializeButtons();
        setupButtonListeners();
        setupVibrator();
        setupRingtone();
        setupNotificationEnvironment();
        updateTimerTextView();
    }

    private void initializeButtons() {
        pieChart = findViewById(R.id.pieChartTimer);
        timerView = findViewById(R.id.textViewTimer);
        startButton = findViewById(R.id.buttonStart);
        resetButton = findViewById(R.id.buttonReset);
        customMinButton = findViewById(R.id.buttonCustomMin);
        oneMinButton = findViewById(R.id.buttonOneMin);
        twoMinButton = findViewById(R.id.buttonTwoMin);
        threeMinButton = findViewById(R.id.buttonThreeMin);
        fiveMinButton = findViewById(R.id.buttonFiveMin);
        tenMinButton = findViewById(R.id.buttonTenMin);
    }

    private void setupButtonListeners() {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTicking) {
                    pauseTimer();
                }
                else {
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

    private void setupRingtone() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
    }

    private void playRingtone() {
        ringtone.play();
    }

    public static void stopRingtone() {
        ringtone.stop();
    }

    private void setupNotificationEnvironment() {
        notificationAssistant = new NotificationAssistant(this);
        notificationAssistant.createNotificationChannel();
        builder = notificationAssistant.createBuilder();
        manager = notificationAssistant.createManager();
    }

    private void askForCustomTime() {
        View dialogView = getLayoutInflater().inflate(R.layout.timer_alert_layout, null);
        EditText minuteText = (EditText) dialogView.findViewById(R.id.editTextTimerAlertMinute);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.timer_alert_provide_time);
        alert.setView(dialogView);
        alert.setPositiveButton(R.string.timer_activity_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Boolean shouldStart = true;
                String minuteString = minuteText.getText().toString();
                long extractedMinutes;
                try {
                    extractedMinutes = Long.parseLong(minuteString);
                    timeLeftInMill = extractedMinutes * SECONDS_TO_MINUTES * MILLISECONDS_TO_SECONDS;
                    lastSelectedTime = timeLeftInMill;
                }
                catch (NumberFormatException e) {
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
            Toast.makeText(TimerActivity.this, getString(R.string.timer_activity_reset_btn_msg), Toast.LENGTH_SHORT).show();
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
                timeDiffStartVsLeft = lastSelectedTime - timeLeftInMill;
                updateTimerTextView();
                updateTimerPieChart();
            }

            @RequiresApi(api = Build.VERSION_CODES.S)
            @Override
            public void onFinish() {
                Toast.makeText(TimerActivity.this, getString(R.string.timer_activity_timer_end_msg), Toast.LENGTH_SHORT).show();
                pauseTimer();
                timerReset();
                playRingtone();
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
            vibrator.vibrate(VibrationEffect.
                    createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE),
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .build());
        }
        else {
            Log.i("Vibrator Issue", "The vibrator initialized to null.");
        }
    }

    private void sendNotification() {
        manager.notify(NOTIFICATION_ID, builder.build());
    }

    private void updateUIHideButtons() {
        buttonDisappear(oneMinButton);
        buttonDisappear(twoMinButton);
        buttonDisappear(threeMinButton);
        buttonDisappear(fiveMinButton);
        buttonDisappear(tenMinButton);
        buttonDisappear(customMinButton);
        Guideline guideline = findViewById(R.id.guidelineHorizontal);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideline.getLayoutParams();
        params.guidePercent = GUIDE_PERCENT_1;
        guideline.setLayoutParams(params);
    }

    private void updateUIShowButtons() {
        Guideline guideline = findViewById(R.id.guidelineHorizontal);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideline.getLayoutParams();
        params.guidePercent = GUIDE_PERCENT_2;
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
                if (!isTicking) {
                    timeLeftInMill = time;
                    lastSelectedTime = time;
                    timeDiffStartVsLeft = 0;    // has to be 0 at this point
                    updateTimerTextView();
                    startTimer();
                }
            }
        });
    }

    private void updateTimerTextView() {
        int seconds = (int) timeLeftInMill / MILLISECONDS_TO_SECONDS % SECONDS_TO_MINUTES;
        int minutes = (int) timeLeftInMill / MILLISECONDS_TO_SECONDS / SECONDS_TO_MINUTES;
        String timeLeft = String.format(Locale.CANADA, "%02d:%02d", minutes, seconds);
        timerView.setText(timeLeft);
    }

    private void updateTimerPieChart() {
        List<PieEntry> pieEntries = new ArrayList<>();
        if (pieEntries.isEmpty()) {
            addPieEntries(pieEntries);
        } else {
            removePieEntries(pieEntries);
            addPieEntries(pieEntries);
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "Time remaining");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.invalidate();
    }

    private void addPieEntries(List<PieEntry> pieEntries) {
        pieEntries.add(new PieEntry(timeLeftInMill));
        pieEntries.add(new PieEntry(timeDiffStartVsLeft));
    }

    private void removePieEntries(List<PieEntry> pieEntries) {
        for (PieEntry entry : pieEntries) {
            pieEntries.remove(entry);
        }
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TimerActivity.class);
    }
}