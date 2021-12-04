package com.example.parentsupportapp.breathingModel;

import android.util.Log;
import android.widget.Toast;

import com.example.parentsupportapp.BreathingActivity;
import com.example.parentsupportapp.R;

public class WaitingState extends State {
    public WaitingState(BreathingActivity context) {
        super(context);
    }

    @Override
    public void handleEnter() {
        super.handleEnter();
        String numBreaths = Integer.toString(context.numberOfBreaths);
        context.txtMain.setText(context.getString(R.string.breathing_header, numBreaths));
        Toast.makeText(context, "Entered waiting state!", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void handleExit() {
        super.handleExit();
        Toast.makeText(context, "Exiting waiting state!", Toast.LENGTH_SHORT).show();
    }
}
