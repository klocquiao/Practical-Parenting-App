package com.example.parentsupportapp.breathingModel;

// State Pattern's base states
public abstract class State {
    // If putting in new file, you might want State base class to
    // hold a reference to the activity
        /*
        private MainActivity context;
        public State(MainActivity context) {
            this.context = context;
        }
        */

    // Empty implementations, so derived class don't need to
    // override methods they don't care about.

    void handleEnter() {}
    void handleExit() {}
    void handleClickOn() {}
    void handleClickOff() {}
}
