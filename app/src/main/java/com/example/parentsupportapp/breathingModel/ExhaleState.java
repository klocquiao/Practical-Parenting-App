package com.example.parentsupportapp.breathingModel;

import android.widget.Toast;

import com.example.parentsupportapp.BreathingActivity;
import com.example.parentsupportapp.R;

public class ExhaleState extends State {
    public ExhaleState(BreathingActivity context) {
        super(context);
    }

    @Override
    public void handleEnter() {
        super.handleEnter();
        context.numberOfBreaths--;

        context.btnBreathe.setEnabled(false);
        handler.postDelayed(exhaleButtonLock, THREE_SECONDS_MS);

        //Start animation & sound

        Toast.makeText(context, "Lets breathe out now...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleExit() {
        super.handleExit();
    }

    Runnable exhaleButtonLock = new Runnable() {
        public void run() {
            if (context.numberOfBreaths > 0) {
                context.btnBreathe.setEnabled(true);
                context.setState(context.inhaleState);
            }
            else {
                context.btnBreathe.setText(R.string.breathing_exercise_complete);
            }
        }
    };
}


