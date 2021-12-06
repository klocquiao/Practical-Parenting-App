package com.example.parentsupportapp;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parentsupportapp.breathingModel.ExhaleState;
import com.example.parentsupportapp.breathingModel.FinishState;
import com.example.parentsupportapp.breathingModel.IdleState;
import com.example.parentsupportapp.breathingModel.StartState;
import com.example.parentsupportapp.breathingModel.InhaleState;
import com.example.parentsupportapp.breathingModel.State;

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

    public int numberOfBreaths;
    public Button btnBreathe;
    public TextView txtMain;
    public ImageView imgBreathingGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathing);
        setupViews();
        setupButtons();
        setupAnimation();
        numberOfBreaths = getIntent().getIntExtra(EXTRA_NUM_OF_BREATHS, 1);
        setState(startState);
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

    public static Intent makeIntent(Context c, int numOfBreaths) {
        Intent intent = new Intent(c, BreathingActivity.class);
        intent.putExtra(EXTRA_NUM_OF_BREATHS, numOfBreaths);
        return intent;
    }
}