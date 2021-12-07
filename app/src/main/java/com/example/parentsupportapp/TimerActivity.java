package com.example.parentsupportapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
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
    public static final long DEFAULT_START_TIME = 300000;
    public static final long ONE_MIN = 60000;
    public static final long TWO_MIN = 120000;
    public static final long THREE_MIN = 180000;
    public static final long FIVE_MIN = 300000;
    public static final long TEN_MIN = 600000;
    public static final int MILLISECONDS_TO_SECONDS = 1000;
    public static final int SECONDS_TO_MINUTES = 60;
    public static final float GUIDE_PERCENT_1 = (float) 0.7;
    public static final float GUIDE_PERCENT_2 = (float) 0.4;
    private static final int NOTIFICATION_ID = 1;
    private static final String KEY_IS_TICKING_KEY = "timerIsTickingKey";
    private static final String KEY_TIME_LEFT_KEY = "timerTimeLeftKey";
    private static final String KEY_TIME_AT_PAUSE = "timerAtPause";
    private static final String KEY_TIME_CURRENT_BASE = "timerCurrentBaseKey";
    private static final String KEY_TIME_LAST_SELECTED_KEY = "timerLastSelectedKey";
    private static final String KEY_TIME_TICK_RATE = "timerTickRateKey";
    private static final String KEY_TIME_TICK_RATE_PERCENT = "timerTickRatePercentKey";
    private static final String PREF_TIMER = "timerActivityPref";
    public static final int DEFAULT_TICK_RATE = 1000;
    public static final float DEFAULT_TICK_RATE_PERCENT = 1;
    public static final int ZERO = 0;

    private PieChart pieChart;
    private TextView timerView;
    private TextView tickRateView;
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
    private long currentBaseTime = DEFAULT_START_TIME;
    private long timeDiffStartVsLeft = ZERO;
    private long timeAtPause = ZERO;
    private long timeAtResume = ZERO;
    private int tickRate = DEFAULT_TICK_RATE;
    private float tickRatePercent = DEFAULT_TICK_RATE_PERCENT;
    private float oldTickRatePercent = DEFAULT_TICK_RATE_PERCENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Toolbar toolbar = findViewById(R.id.toolbarTimer);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.timer_title);
        }

        initializeButtons();
        setupButtonListeners();
        setupVibrator();
        setupRingtone();
        setupNotificationEnvironment();
        updateTimerTextView();
        setupPieChart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.menu_timer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_accelerate_time:
                if (tickRate > 250 && isTicking) {
                    accelerateTime();
                    scaleTimeValues();
                }
                return true;

            case R.id.action_decelerate_time:
                if (tickRate < 4000 && isTicking) {
                    decelerateTime();
                    scaleTimeValues();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void accelerateTime() {
        oldTickRatePercent = tickRatePercent;
        if (tickRatePercent < 1.0) {
            tickRatePercent += 0.25;
        }
        else {
            tickRatePercent += 1;
        }
    }

    private void decelerateTime() {
        oldTickRatePercent = tickRatePercent;
        if (tickRatePercent <= 1) {
            tickRatePercent -= 0.25;
        } else {
            tickRatePercent -= 1;
        }
    }

    private void scaleTimeValues() {
        tickRate = (int) (DEFAULT_TICK_RATE / tickRatePercent);
        timeLeftInMill = (long) (timeLeftInMill * oldTickRatePercent);
        timeLeftInMill = (long) (timeLeftInMill / tickRatePercent);
        currentBaseTime = (long) (currentBaseTime * oldTickRatePercent);
        currentBaseTime = (long) (currentBaseTime / tickRatePercent);
        timeDiffStartVsLeft = currentBaseTime - timeLeftInMill;
        updateTimerTextView();
        updateTimerPieChart();
        pauseTimer();
        startTimer();
        updateTickRateView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadTimerActivityPrefs(this);
        if (isTicking) {
            timeAtResume = Calendar.getInstance().getTimeInMillis();
            timeLeftInMill = timeLeftInMill - (timeAtResume - timeAtPause);
            startTimer();
        } else {
            timeAtPause = 0;
            timeAtResume = 0;
            tickRate = DEFAULT_TICK_RATE;
            tickRatePercent = DEFAULT_TICK_RATE_PERCENT;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveTimerActivityPrefs(this);
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
        tickRateView = findViewById(R.id.textViewTickRate);
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

    private void setupPieChart() {
        hidePieChart();
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
        Description description = pieChart.getDescription();
        description.setEnabled(false);
        pieChart.setHoleRadius(0);
        pieChart.setDrawCenterText(false);
    }

    private void showPieChart() {
        pieChart.setVisibility(View.VISIBLE);
    }

    private void hidePieChart() {
        pieChart.setVisibility(View.GONE);
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
                boolean shouldStart = true;
                String minuteString = minuteText.getText().toString();
                long extractedMinutes;
                try {
                    extractedMinutes = Long.parseLong(minuteString);
                    timeLeftInMill = extractedMinutes * SECONDS_TO_MINUTES * MILLISECONDS_TO_SECONDS;
                    currentBaseTime = timeLeftInMill;
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
            currentBaseTime = lastSelectedTime;
            timeAtResume = 0;
            timeAtPause = 0;
            tickRate = DEFAULT_TICK_RATE;
            tickRatePercent = DEFAULT_TICK_RATE_PERCENT;
            updateTimerTextView();
            updateUIShowButtons();
            hidePieChart();
            startButton.setText(R.string.timerActivity_start);
        }
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        isTicking = false;
        startButton.setText(R.string.timerActivity_start);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMill, tickRate) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMill = millisUntilFinished;
                timeDiffStartVsLeft = currentBaseTime - timeLeftInMill;
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
        updateTickRateView();
        showPieChart();
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
        tickRateView.setVisibility(View.VISIBLE);
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
        tickRateView.setVisibility(View.INVISIBLE);
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
                    currentBaseTime = time;
                    lastSelectedTime = time;
                    timeDiffStartVsLeft = 0;
                    updateTimerTextView();
                    startTimer();
                }
            }
        });
    }

    private void updateTimerTextView() {
        int localTimeLeftInMill = (int) (timeLeftInMill * tickRatePercent);
        int seconds = (int) (localTimeLeftInMill / MILLISECONDS_TO_SECONDS) % SECONDS_TO_MINUTES;
        int minutes = (int) (localTimeLeftInMill / MILLISECONDS_TO_SECONDS / SECONDS_TO_MINUTES);
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

        PieDataSet dataSet = new PieDataSet(pieEntries, getString(R.string.timer_pie_chart_data_set_label));
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.invalidate();
    }

    private void addPieEntries(List<PieEntry> pieEntries) {
        int secondLeft = (int) (timeLeftInMill);
        pieEntries.add(new PieEntry(secondLeft));
        int secondDiff = (int) (timeDiffStartVsLeft);
        pieEntries.add(new PieEntry(secondDiff));
    }

    private void removePieEntries(List<PieEntry> pieEntries) {
        for (PieEntry entry : pieEntries) {
            pieEntries.remove(entry);
        }
    }

    private void updateTickRateView() {
        String newTickRate = String.format(Locale.CANADA, "Time @%.0f", (tickRatePercent * 100));
        tickRateView.setText(newTickRate);
    }

    private void loadTimerActivityPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_TIMER, MODE_PRIVATE);
        if (prefs.contains(KEY_IS_TICKING_KEY)) {
            isTicking = prefs.getBoolean(KEY_IS_TICKING_KEY, false);
            timeLeftInMill = prefs.getLong(KEY_TIME_LEFT_KEY, DEFAULT_START_TIME);
            currentBaseTime = prefs.getLong(KEY_TIME_CURRENT_BASE, DEFAULT_START_TIME);
            lastSelectedTime = prefs.getLong(KEY_TIME_LAST_SELECTED_KEY, DEFAULT_START_TIME);
            timeAtPause = prefs.getLong(KEY_TIME_AT_PAUSE, 0);
            tickRate = prefs.getInt(KEY_TIME_TICK_RATE, DEFAULT_TICK_RATE);
            tickRatePercent = prefs.getFloat(KEY_TIME_TICK_RATE_PERCENT, DEFAULT_TICK_RATE_PERCENT);
        }
    }

    private void saveTimerActivityPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_TIMER, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_IS_TICKING_KEY, isTicking);
        editor.putLong(KEY_TIME_LEFT_KEY, timeLeftInMill);
        editor.putLong(KEY_TIME_CURRENT_BASE, currentBaseTime);
        editor.putLong(KEY_TIME_LAST_SELECTED_KEY, lastSelectedTime);
        editor.putInt(KEY_TIME_TICK_RATE, tickRate);
        editor.putFloat(KEY_TIME_TICK_RATE_PERCENT, tickRatePercent);
        timeAtPause = Calendar.getInstance().getTimeInMillis();
        editor.putLong(KEY_TIME_AT_PAUSE, timeAtPause);
        editor.apply();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TimerActivity.class);
    }
}