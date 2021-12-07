package com.example.parentsupportapp.model.breathingModel;

import com.example.parentsupportapp.BreathingActivity;
import com.example.parentsupportapp.R;

/**
 * First state entered in the BreathingActivity exercise. This state handles the situation
 * where the user first boots up the exercise and technically the user isn't exhaling or breathing.
 */

public class StartState extends State{
    public StartState(BreathingActivity context) {
        super(context);
    }

    @Override
    public void handleEnter() {
        super.handleEnter();
        String numBreaths = Integer.toString(context.numberOfBreaths);
        context.txtMain.setText(context.getString(R.string.breathing_header, numBreaths));
        context.btnBreathe.setText(context.getString(R.string.breathing_in));
    }

    @Override
    public void handleClick() {
        super.handleClick();
        context.setState(context.inhaleState);
        context.currentState.handleClick();
    }
}
