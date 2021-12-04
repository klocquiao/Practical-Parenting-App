package com.example.parentsupportapp.breathingModel;

import com.example.parentsupportapp.BreathingActivity;
import com.example.parentsupportapp.R;

public class FinishState extends State {
    public FinishState(BreathingActivity context) {
        super(context);
    }

    @Override
    public void handleEnter() {
        super.handleEnter();
        context.btnBreathe.setText(R.string.breathing_exercise_complete);
    }
}
