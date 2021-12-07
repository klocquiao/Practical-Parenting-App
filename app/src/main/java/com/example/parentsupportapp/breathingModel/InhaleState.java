package com.example.parentsupportapp.breathingModel;

import android.widget.TextView;
import android.widget.Toast;

import com.example.parentsupportapp.BreathingActivity;
import com.example.parentsupportapp.R;

/**
 * Inhale state of the BreathingActivity exercise. Occurs when the user has let go of the
 * button after >3 seconds.
 */

public class InhaleState extends State {
    public InhaleState(BreathingActivity context) {
        super(context);
    }

    @Override
    public void handleEnter() {
        super.handleEnter();

        context.imgBreathingGuide.setColorFilter(context.getColor(R.color.breathing_green));
        context.tvPrompt.setText(R.string.breathing_inhale_help);
    }

    @Override
    public void handleExit() {
        super.handleExit();
        context.growCircle.cancel();
        context.stopSound(context.inhaleSound);
    }

    @Override
    public void handleButtonHold(long seconds) {
        super.handleButtonHold(seconds);
        handler.removeCallbacks(canBreatheOut);
        handler.removeCallbacks(shouldBreathOut);
        if (seconds <= THREE_SECONDS_MS) {
            Toast.makeText(context, context.getString(R.string.breathing_inhale_error), Toast.LENGTH_SHORT).show();
            context.growCircle.cancel();
            context.stopSound(context.inhaleSound);
        }
        else {
            context.tvPrompt.setText(R.string.breathing_out_prompt);
            context.setState(context.exhaleState);
        }
    }

    @Override
    public void handleClick() {
        super.handleClick();
        context.growCircle.start();
        context.inhaleSound.start();
        handler.postDelayed(canBreatheOut, THREE_SECONDS_MS);
        handler.postDelayed(shouldBreathOut, TEN_SECONDS_MS);
    }

    Runnable shouldBreathOut = new Runnable() {
        public void run() {
            context.tvPrompt.setText(R.string.breathing_exhale_error);
        }
    };

    Runnable canBreatheOut = new Runnable() {
        public void run() {
            context.tvPrompt.setText(R.string.breathing_out_prompt);
            context.btnBreathe.setText(context.getString(R.string.breathing_out));
        }
    };
}
