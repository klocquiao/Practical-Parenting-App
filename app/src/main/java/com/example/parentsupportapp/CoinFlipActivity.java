package com.example.parentsupportapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.parentsupportapp.model.Child;
import com.example.parentsupportapp.model.Family;

import java.util.Random;

public class CoinFlipActivity extends AppCompatActivity {

    private AnimatorSet coinFlipAnimation;
    private Animator coinFlip1Animation;
    private Animator coinFlip2Animation;

    private AnimatorSet transitionUpAnimation;
    private AnimatorSet transitionDownAnimation;
    private MediaPlayer coinFlipSound;

    private ImageView head;
    private ImageView tail;
    private Spinner childrenSpinner;

    private boolean isHead;
    private boolean isFlipping;

    private Family family;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        isHead = true;
        head = findViewById(R.id.head);
        tail = findViewById(R.id.tail);

        family = Family.getInstance();

        populateChildrenSpinner();
        setupCoinFlipAnimation();
        setupFlipButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateChildrenSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_flip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.actionHistory:
                Intent historyIntent = HistoryActivity.makeIntent(this);
                startActivity(historyIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void populateChildrenSpinner() {
        childrenSpinner = (Spinner) findViewById(R.id.spinnerChildren);
        ArrayAdapter<Child> adapter = new ArrayAdapter<Child>(this,
                android.R.layout.simple_spinner_item, family.getChildren());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        childrenSpinner.setAdapter(adapter);
    }

    private void setupCoinFlipAnimation() {
        coinFlipAnimation = new AnimatorSet();
        transitionUpAnimation = new AnimatorSet();
        transitionDownAnimation = new AnimatorSet();

        //Initialize animation components
        coinFlip1Animation = AnimatorInflater.loadAnimator(this,
                R.animator.coin_flip_animator_1);

        coinFlip2Animation = AnimatorInflater.loadAnimator(this,
                R.animator.coin_flip_animator_2);

        coinFlipAnimation.playTogether(coinFlip1Animation, coinFlip2Animation);

        //Create the "up and down" animation for the coin flip
        ObjectAnimator transitionUpHead = ObjectAnimator.ofFloat(head, "translationY", -250f);
        ObjectAnimator transitionUpTail = ObjectAnimator.ofFloat(tail, "translationY", -250f);
        transitionUpAnimation.playTogether(transitionUpHead, transitionUpTail);
        transitionUpAnimation.setInterpolator(new AnticipateOvershootInterpolator());

        ObjectAnimator transitionDownHead = ObjectAnimator.ofFloat(head, "translationY", 0f);
        ObjectAnimator transitionDownTail = ObjectAnimator.ofFloat(tail, "translationY", 0f);
        transitionDownAnimation.playTogether(transitionDownHead, transitionDownTail);
        transitionDownAnimation.setInterpolator(new BounceInterpolator());

        //Change camera distance for head and tail images
        head.setCameraDistance(8000);
        tail.setCameraDistance(8000);

        //Create coin flip sound
        coinFlipSound = MediaPlayer.create(this, R.raw.coinflipsound);

        coinFlipAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (isFlipping) {
                    checkHeadsOrTails();
                    coinFlipAnimation.play(coinFlip1Animation).with(coinFlip2Animation);
                    coinFlipAnimation.start();
                }
            }

            public void onAnimationStart(Animator animation) {
                isFlipping = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isFlipping = false;
            }
        });
    }

    private void setupFlipButton() {
        Random random = new Random();
        Handler handler = new Handler();
        Button flipButton = findViewById(R.id.flip);

        flipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipButton.setEnabled(false);
                int delay;

                if (random.nextBoolean()) {
                    transitionUpAnimation.setDuration(600);
                    transitionDownAnimation.setDuration(1000);
                    transitionDownAnimation.setStartDelay(600);
                    delay = 1200;

                }
                else {
                    transitionUpAnimation.setDuration(700);
                    transitionDownAnimation.setDuration(1100);
                    transitionDownAnimation.setStartDelay(700);
                    delay = 1400;
                }

                checkHeadsOrTails();
                coinFlipSound.start();
                transitionUpAnimation.start();
                transitionDownAnimation.start();
                coinFlipAnimation.start();

                //Wait for coin flip animation to finish
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        coinFlipAnimation.cancel();
                        head.setRotationX(0);
                        tail.setRotationX(0);
                    }
                }, delay);

                //Wait for bounce interpolation to finish
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        flipButton.setEnabled(true);
                    }
                }, delay + 400);
            }
        });
    }

    private void checkHeadsOrTails() {
        if (isHead) {
            coinFlip1Animation.setTarget(head);
            coinFlip2Animation.setTarget(tail);
            isHead = false;
        }
        else {
            coinFlip1Animation.setTarget(tail);
            coinFlip2Animation.setTarget(head);
            isHead = true;
        }
    }

    public static Intent makeIntent(Context c) {
        Intent intent = new Intent(c, CoinFlipActivity.class);
        return intent;
    }
}
