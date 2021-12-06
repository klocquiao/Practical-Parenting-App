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
        context.tvPrompt.setText("Breathing finished");

        String numBreaths = Integer.toString(context.numberOfBreaths);
        context.txtMain.setText(context.getString(R.string.breathing_header, numBreaths));
    }
}
