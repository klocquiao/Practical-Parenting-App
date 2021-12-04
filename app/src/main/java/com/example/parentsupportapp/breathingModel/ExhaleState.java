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
        handler.postDelayed(stateToExhale, TEN_SECONDS_MS);

        //Start animation & sound

        Toast.makeText(context, context.getString(R.string.breathing_exhale_help), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleExit() {
        super.handleExit();
        handler.removeCallbacks(stateToExhale);
        //Cancel animations & sound
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
        context.setState(context.inhaleState);
        context.currentState.handleClick();
    }
}


