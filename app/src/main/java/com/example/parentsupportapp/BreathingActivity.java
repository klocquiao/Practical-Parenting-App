package com.example.parentsupportapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.parentsupportapp.model.breathingModel.ExhaleState;
import com.example.parentsupportapp.model.breathingModel.FinishState;
import com.example.parentsupportapp.model.breathingModel.IdleState;
import com.example.parentsupportapp.model.breathingModel.StartState;
import com.example.parentsupportapp.model.breathingModel.InhaleState;
import com.example.parentsupportapp.model.breathingModel.State;

/**
 * The BreathingActivity is the main control for the breathing exercise.
 * It will guide the user through it, changing the state as necessary.
 * State changes push the exercise further till there are 0 breaths left to take
 */

public class BreathingActivity extends AppCompatActivity {
    private static final String EXTRA_NUM_OF_BREATHS = "breathingActivity.num_of_breaths";
    public final State inhaleState = new InhaleState(this);
    public final State exhaleState = new ExhaleState(this);
    public final State finishState = new FinishState(this);
    public final State startState = new StartState(this);
    public State currentState = new IdleState(this);

    public AnimatorSet growCircle;
    public ObjectAnimator growCircleX;
    public ObjectAnimator growCircleY;

    public AnimatorSet shrinkCircle;
    public ObjectAnimator shrinkCircleX;
    public ObjectAnimator shrinkCircleY;

    public MediaPlayer inhaleSound;
    public MediaPlayer exhaleSound;

    public int numberOfBreaths;
    public Button btnBreathe;
    public TextView txtMain;
    public TextView tvPrompt;
    public ImageView imgBreathingGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathing);
        Toolbar toolbar = findViewById(R.id.toolbarBreathing);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        setupViews();
        setupButtons();
        setupAnimation();
        setupSound();
        numberOfBreaths = getIntent().getIntExtra(EXTRA_NUM_OF_BREATHS, 1);
        setState(startState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        shrinkCircle.cancel();
        growCircle.cancel();
        inhaleSound.stop();
        exhaleSound.stop();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setupButtons() {
        btnBreathe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    currentState.handleClick();
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    long timeHeld = motionEvent.getEventTime() - motionEvent.getDownTime();
                    currentState.handleButtonHold(timeHeld);
                }
                return false;
            }
        });
    }

    public void setupViews() {
        btnBreathe = findViewById(R.id.btnBreathe);
        txtMain = findViewById(R.id.tvDemo);
        tvPrompt = findViewById(R.id.tvBreathingPrompt);
        imgBreathingGuide = findViewById(R.id.imgBreathingGuide);
    }

    public void setState(State newState) {
        currentState.handleExit();
        currentState = newState;
        currentState.handleEnter();
    }

    public void setupAnimation() {
        shrinkCircle = new AnimatorSet();
        shrinkCircleX = ObjectAnimator.ofFloat(imgBreathingGuide, "scaleX", 1.0f);
        shrinkCircleY = ObjectAnimator.ofFloat(imgBreathingGuide, "scaleY", 1.0f);
        shrinkCircle.playTogether(shrinkCircleX, shrinkCircleY);
        shrinkCircle.setInterpolator(new DecelerateInterpolator());
        shrinkCircle.setDuration(State.TEN_SECONDS_MS);

        growCircle = new AnimatorSet();
        growCircleX = ObjectAnimator.ofFloat(imgBreathingGuide, "scaleX", 1.8f);
        growCircleY = ObjectAnimator.ofFloat(imgBreathingGuide, "scaleY", 1.8f);
        growCircle.playTogether(growCircleX, growCircleY);
        growCircle.setInterpolator(new DecelerateInterpolator());
        growCircle.setDuration(State.TEN_SECONDS_MS);
    }

    public void setupSound() {
        inhaleSound = MediaPlayer.create(this, R.raw.breathing_activity_inhale);
        exhaleSound = MediaPlayer.create(this, R.raw.breathing_activity_exhale);
    }

    public void stopSound(MediaPlayer sound) {
        sound.pause();
        sound.seekTo(0);
    }

    public static Intent makeIntent(Context c, int numOfBreaths) {
        Intent intent = new Intent(c, BreathingActivity.class);
        intent.putExtra(EXTRA_NUM_OF_BREATHS, numOfBreaths);
        return intent;
    }
}