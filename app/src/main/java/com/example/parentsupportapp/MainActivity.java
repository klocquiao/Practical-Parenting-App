package com.example.parentsupportapp;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parentsupportapp.databinding.ActivityMainBinding;

import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

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
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

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
                }, 5000);

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

    private void tailToHead() {
        coinFlip1Animation.setTarget(tail);
        coinFlip2Animation.setTarget(head);
        coinFlip2Animation.start();
        coinFlip1Animation.start();
        isHead = true;
    }


}