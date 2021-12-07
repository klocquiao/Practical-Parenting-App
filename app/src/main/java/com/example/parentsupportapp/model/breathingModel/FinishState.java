package com.example.parentsupportapp.model.breathingModel;

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
        context.tvPrompt.setText(R.string.breathing_finished);

        String numBreaths = Integer.toString(context.numberOfBreaths);
        context.txtMain.setText(context.getString(R.string.breathing_header, numBreaths));
        Toast.makeText(context, R.string.breathing_finished, Toast.LENGTH_SHORT).show();

        handler.postDelayed(terminateActivity, SEVEN_SECONDS_MS);
    }

    Runnable terminateActivity = new Runnable() {
        public void run() {
            context.finish();
        }
    };
}
