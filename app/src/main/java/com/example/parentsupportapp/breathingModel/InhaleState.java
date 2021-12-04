package com.example.parentsupportapp.breathingModel;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.parentsupportapp.BreathingActivity;
import com.example.parentsupportapp.CoinFlipActivity;
import com.example.parentsupportapp.R;
import com.example.parentsupportapp.model.Child;


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

        Toast.makeText(context, "Entered inhale state!", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void handleExit() {
        super.handleExit();
        Toast.makeText(context, "Exiting inhale state!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleButtonHold(long seconds) {
        super.handleButtonHold(seconds);
        handler.removeCallbacks(displayHelpMessage);
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
        Toast.makeText(context, "Starting breathing animation!", Toast.LENGTH_SHORT).show();
        //1. Start Animation
        //2. Start sound
        handler.postDelayed(displayHelpMessage, TEN_SECONDS_MS);
    }

    Runnable displayHelpMessage = new Runnable() {
        public void run() {
            Toast.makeText(context, "Release button and breathe!", Toast.LENGTH_SHORT).show();
        }
    };
}
