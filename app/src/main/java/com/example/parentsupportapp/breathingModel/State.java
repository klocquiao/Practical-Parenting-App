package com.example.parentsupportapp.breathingModel;

import android.os.Handler;

import com.example.parentsupportapp.BreathingActivity;

// State Pattern's base states
public abstract class State {
    public static final long THREE_SECONDS_MS = 3000;
    public static final long TEN_SECONDS_MS = 10000;
    protected Handler handler;
    protected BreathingActivity context;

    public State(BreathingActivity context) {
        this.context = context;
        this.handler = new Handler();
    }

    // Empty implementations, so derived class don't need to
    // override methods they don't care about.

    public void handleEnter() {}
    public void handleExit() {}
    public void handleButtonHold(long seconds) {}
    public void handleButtonLongHold() {}
    public void handleClick() {}
}
