package com.example.parentsupportapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parentsupportapp.breathingModel.ExhaleState;
import com.example.parentsupportapp.breathingModel.FinishState;
import com.example.parentsupportapp.breathingModel.IdleState;
import com.example.parentsupportapp.breathingModel.InhaleState;
import com.example.parentsupportapp.breathingModel.State;
import com.example.parentsupportapp.breathingModel.WaitingState;

public class BreathingActivity extends AppCompatActivity {
    private static final String EXTRA_NUM_OF_BREATHS = "breathingActivity.num_of_breaths";
    private int numberOfBreaths;
    public final State inhaleState = new InhaleState(this);
    public final State exhaleState = new ExhaleState(this);
    public final State waitingState = new WaitingState(this);
    public final State finishState = new FinishState(this);
    private State currentState = new IdleState(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathing);

        setState(waitingState);
        numberOfBreaths = 1;
    }


    public void setState(State newState) {
        currentState.handleExit();
        currentState = newState;
        currentState.handleEnter();
    }

    public static Intent makeIntent(Context c, int numOfBreaths) {
        Intent intent = new Intent(c, BreathingActivity.class);
        intent.putExtra(EXTRA_NUM_OF_BREATHS, numOfBreaths);
        return intent;
    }
}