package com.example.parentsupportapp.breathingModel;

import android.widget.TextView;
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

        context.imgBreathingGuide.setColorFilter(context.getColor(R.color.breathing_green));
        Toast.makeText(context, context.getString(R.string.breathing_inhale_help), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleExit() {
        super.handleExit();
        context.growCircle.cancel();
        //2. Stop sound
    }

    @Override
    public void handleButtonHold(long seconds) {
        super.handleButtonHold(seconds);
        handler.removeCallbacks(canBreatheOut);
        handler.removeCallbacks(shouldBreathOut);
        if (seconds <= THREE_SECONDS_MS) {
            Toast.makeText(context, context.getString(R.string.breathing_inhale_error), Toast.LENGTH_SHORT).show();
            context.growCircle.cancel();
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
        context.growCircle.start();
        //2. Start sound
        handler.postDelayed(canBreatheOut, THREE_SECONDS_MS);
        handler.postDelayed(shouldBreathOut, TEN_SECONDS_MS);
    }

    Runnable shouldBreathOut = new Runnable() {
        public void run() {
            Toast.makeText(context, context.getString(R.string.breathing_exhale_error), Toast.LENGTH_SHORT).show();
        }
    };

    Runnable canBreatheOut = new Runnable() {
        public void run() {
            context.btnBreathe.setText(context.getString(R.string.breathing_out));
        }
    };
}
