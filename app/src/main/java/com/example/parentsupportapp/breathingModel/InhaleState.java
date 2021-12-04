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
        Toast.makeText(context, "Entered inhale state!", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void handleExit() {
        super.handleExit();
        Toast.makeText(context, "Exiting inhale state!", Toast.LENGTH_SHORT).show();
    }
}
