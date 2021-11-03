package com.example.parentsupportapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class CoinFlipActivity extends AppCompatActivity {

    private Animator coinFlip1Animation;
    private Animator coinFlip2Animation;
    private AnimatorSet coinFlipAnimation;
    private ObjectAnimator transitionUpHead;
    private ObjectAnimator transitionUpTail;
    private AnimatorSet transitionUpAnimation;
    private ObjectAnimator transitionDownHead;
    private ObjectAnimator transitionDownTail;
    private AnimatorSet transitionDownAnimation;

    private Handler handler;
    private ImageView head;
    private ImageView tail;
    private Button flipButton;

    private boolean isHead;
    private boolean isFlipping;

    private Random random;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
        isHead = true;

        random = new Random();

        head = findViewById(R.id.head);
        tail = findViewById(R.id.tail);

        flipButton = findViewById(R.id.flip);

        handler = new Handler();

        coinFlip1Animation = AnimatorInflater.loadAnimator(this,
                R.animator.coin_flip_animator_1);

        coinFlip2Animation = AnimatorInflater.loadAnimator(this,
                R.animator.coin_flip_animator_2);

        coinFlipAnimation = new AnimatorSet();
        transitionUpAnimation = new AnimatorSet();
        transitionDownAnimation = new AnimatorSet();

        transitionUpHead = ObjectAnimator.ofFloat(head, "translationY", -250f);
        transitionUpTail = ObjectAnimator.ofFloat(tail, "translationY", -250f);
        transitionUpAnimation.playTogether(transitionUpHead, transitionUpTail);
        transitionUpAnimation.setInterpolator(new AnticipateOvershootInterpolator());

        transitionDownHead = ObjectAnimator.ofFloat(head, "translationY", 0f);
        transitionDownTail = ObjectAnimator.ofFloat(tail, "translationY", 0f);
        transitionDownAnimation.playTogether(transitionDownHead, transitionDownTail);
        transitionDownAnimation.setInterpolator(new BounceInterpolator());

        head.setCameraDistance(8000);
        tail.setCameraDistance(8000);

       coinFlipAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (isFlipping) {
                    headOrTail();
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
                    transitionDownAnimation.setDuration(1200);
                    transitionDownAnimation.setStartDelay(700);
                    delay = 1400;
                }

                headOrTail();
                coinFlipAnimation.play(coinFlip1Animation).with(coinFlip2Animation);
                transitionUpAnimation.start();
                transitionDownAnimation.start();
                coinFlipAnimation.start();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        coinFlipAnimation.cancel();
                        head.setRotationX(0);
                        tail.setRotationX(0);
                        flipButton.setEnabled(true);
                    }
                }, delay);

            }
        });
    }

    private void headOrTail() {
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

    public static Intent getIntent(Context c) {
        Intent intent = new Intent(c, CoinFlipActivity.class);
        return intent;
    }
}
