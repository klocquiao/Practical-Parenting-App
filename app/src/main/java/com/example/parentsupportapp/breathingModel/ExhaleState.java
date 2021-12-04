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

        String numBreaths = Integer.toString(context.numberOfBreaths);
        context.txtMain.setText(context.getString(R.string.breathing_header, numBreaths));

        context.btnBreathe.setText(context.getString(R.string.breathing_out));

        context.btnBreathe.setEnabled(false);
        handler.postDelayed(exhaleButtonLock, THREE_SECONDS_MS);
        Toast.makeText(context, "Entered exhale state!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleExit() {
        super.handleExit();
        //cancel animations
        Toast.makeText(context, "Can inhale again!", Toast.LENGTH_SHORT).show();
    }

    Runnable exhaleButtonLock = new Runnable() {
        public void run() {
            context.btnBreathe.setEnabled(true);
            if (context.numberOfBreaths > 0) {
                context.setState(context.inhaleState);
            }
            else {
                Toast.makeText(context, "Finished!", Toast.LENGTH_SHORT).show();
            }
        }
    };
}


