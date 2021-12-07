package com.example.parentsupportapp.breathingModel;

import android.widget.Toast;

import com.example.parentsupportapp.BreathingActivity;
import com.example.parentsupportapp.R;

/**
 * Last state entered in the BreathingActivity exercise.
 */

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
        Toast.makeText(context, "Breathing finished. Good Job!", Toast.LENGTH_SHORT).show();
        context.finish();
    }
}
