package com.example.parentsupportapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class CoinFlipActivity extends AppCompatActivity {

    private Animator coinFlip1Animation;
    private Animator coinFlip2Animation;
    private AnimatorSet coinFlipAnimation;

    private Handler handler;
    private ImageView head;
    private ImageView tail;
    private Button flipButton;

    private boolean isHead;
    private boolean isFlipping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
        isHead = true;

        head = findViewById(R.id.head);
        tail = findViewById(R.id.tail);

        flipButton = findViewById(R.id.flip);

        handler = new Handler();

        coinFlip1Animation = AnimatorInflater.loadAnimator(this,
                R.animator.coin_flip_animator_1);

        coinFlip2Animation = AnimatorInflater.loadAnimator(this,
                R.animator.coin_flip_animator_2);

        coinFlipAnimation = new AnimatorSet();

        head.setCameraDistance(8000);
        tail.setCameraDistance(8000);

        coinFlipAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (isFlipping == true) {
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
                headOrTail();
                coinFlipAnimation.play(coinFlip1Animation).with(coinFlip2Animation);
                coinFlipAnimation.start();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        coinFlipAnimation.cancel();
                    }
                }, 2500);

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
