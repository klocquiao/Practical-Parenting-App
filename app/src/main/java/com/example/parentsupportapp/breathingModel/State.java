package com.example.parentsupportapp.breathingModel;

import com.example.parentsupportapp.BreathingActivity;

// State Pattern's base states
public abstract class State {

        protected BreathingActivity context;

        public State(BreathingActivity context) {
            this.context = context;
        }

    // Empty implementations, so derived class don't need to
    // override methods they don't care about.

    public void handleEnter() {}
    public void handleExit() {}
    public void handleClickOn() {}
    public void handleClickOff() {}
}
