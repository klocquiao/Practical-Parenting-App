package com.example.parentsupportapp.breathingModel;

import android.widget.Toast;

import com.example.parentsupportapp.BreathingActivity;
import com.example.parentsupportapp.R;

/**
 * Exhale state of the BreathingActivity exercise. Occurs when button is held down and
 * user also stays in this state if they fail their inhale.
 */

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
        handler.postDelayed(stateToExhale, TEN_SECONDS_MS);

        context.imgBreathingGuide.setColorFilter(context.getColor(R.color.breathing_red));
        context.shrinkCircle.start();
        context.exhaleSound.start();

        context.tvPrompt.setText(R.string.breathing_out_prompt);
    }

    @Override
    public void handleExit() {
        super.handleExit();
        handler.removeCallbacks(stateToExhale);

    }

    Runnable exhaleButtonLock = new Runnable() {
        public void run() {
            String numBreaths = Integer.toString(context.numberOfBreaths);
            context.txtMain.setText(context.getString(R.string.breathing_header, numBreaths));
            if (context.numberOfBreaths > 0) {
                context.btnBreathe.setEnabled(true);
                context.btnBreathe.setText(context.getString(R.string.breathing_in));
            }
            else {
                context.setState(context.finishState);
            }
        }
    };

    Runnable stateToExhale = new Runnable() {
        public void run() {
            context.setState(context.inhaleState);
        }
    };

    @Override
    public void handleClick() {
        super.handleClick();
        context.stopSound(context.exhaleSound);
        context.shrinkCircle.cancel();
        context.setState(context.inhaleState);
        context.currentState.handleClick();
    }
}


