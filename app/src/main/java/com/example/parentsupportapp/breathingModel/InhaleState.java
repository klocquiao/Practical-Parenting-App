package com.example.parentsupportapp.breathingModel;

import android.widget.Toast;

import com.example.parentsupportapp.BreathingActivity;
import com.example.parentsupportapp.R;


public class InhaleState extends State {
    public InhaleState(BreathingActivity context) {
        super(context);
    }

    @Override
    public void handleEnter() {
        super.handleEnter();

        String numBreaths = Integer.toString(context.numberOfBreaths);
        context.txtMain.setText(context.getString(R.string.breathing_header, numBreaths));

        context.btnBreathe.setText(context.getString(R.string.breathing_in));

        Toast.makeText(context, "Breathe in and hold the button...", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void handleExit() {
        super.handleExit();
        //1. Revert animation
        //2. Stop sound
    }

    @Override
    public void handleButtonHold(long seconds) {
        super.handleButtonHold(seconds);
        handler.removeCallbacks(canBreatheOut);
        handler.removeCallbacks(shouldBreathOut);
        if (seconds <= THREE_SECONDS_MS) {
            Toast.makeText(context, "Breathe for longer!", Toast.LENGTH_SHORT).show();
            //1. Revert animation
            //2. Stop sound
            //3. Back to waiting
        }
        else {
            context.setState(context.exhaleState);
        }
    }

    @Override
    public void handleClick() {
        super.handleClick();
        //1. Start Animation
        //2. Start sound
        handler.postDelayed(canBreatheOut, THREE_SECONDS_MS);
        handler.postDelayed(shouldBreathOut, TEN_SECONDS_MS);
    }

    Runnable shouldBreathOut = new Runnable() {
        public void run() {
            Toast.makeText(context, "Release the button and breathe out!", Toast.LENGTH_SHORT).show();
        }
    };

    Runnable canBreatheOut = new Runnable() {
        public void run() {
            context.btnBreathe.setText(context.getString(R.string.breathing_out));
        }
    };
}
