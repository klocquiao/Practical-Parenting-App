package com.example.parentsupportapp.model.breathingModel;

import android.os.Handler;

import com.example.parentsupportapp.BreathingActivity;

/**
 * The abstract implementation for states in BreathingActivity exercise.
 */

public abstract class State {
    public static final long THREE_SECONDS_MS = 3000;
    public static final long SEVEN_SECONDS_MS = 7000;
    public static final long TEN_SECONDS_MS = 10000;
    protected Handler handler;
    protected BreathingActivity context;

    public State(BreathingActivity context) {
        this.context = context;
        this.handler = new Handler();
    }

    public void handleEnter() {}
    public void handleExit() {}
    public void handleButtonHold(long seconds) {}
    public void handleClick() {}
}
