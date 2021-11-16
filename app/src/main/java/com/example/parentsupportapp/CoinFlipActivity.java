/**
 * CoinFlipActivity allows the user to flip a coin for making decisions. It allows the user
 * to choose from a list of children (if there is any) and then let them decide whether
 * they want to choose heads or tails. Flipping the coin will play a realistic coin flip animation
 * and then will clearly display the results. The coin flips associated data is then saved in the history.
 * Lastly the app will make a suggestion as to who should go next based on least recently flipped.
 */

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parentsupportapp.model.Child;
import com.example.parentsupportapp.model.Family;
import com.example.parentsupportapp.model.HistoryEntry;
import com.example.parentsupportapp.model.HistoryManager;

import java.util.Random;

public class CoinFlipActivity extends AppCompatActivity {
    private static final String HEADS = "Heads";
    private static final String TAILS = "Tails";
    public static final float ANIM_HEIGHT = -250f;
    public static final float BASE_HEIGHT = 0f;
    public static final int BOUNCE_INTERPOLATION_DELAY = 400;
    public static final int EVEN_FLIPS_TIME = 1200;
    public static final int ODD_FLIP_TIME = 1400;
    public static final int CAM_DISTANCE = 10000;

    private AnimatorSet coinFlipAnimation;
    private Animator coinFlip1Animation;
    private Animator coinFlip2Animation;

    private AnimatorSet transitionUpAnimation;
    private AnimatorSet transitionDownAnimation;
    private MediaPlayer coinFlipSound;

    private Handler handler;
    private Random random;

    private ImageView coinHeadsImage;
    private ImageView coinTailsImage;
    private Spinner childrenSpinner;
    private Button flipButton;
    private TextView coinFlipSuggestionText;
    private RadioGroup headsOrTailsGroup;

    private boolean isHead;
    private boolean isFlipping;

    private Family family;
    private HistoryManager history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        isHead = true;

        initializeViews();

        random = new Random();
        handler = new Handler();

        family = Family.getInstance(this);
        history = HistoryManager.getInstance(this);

        updateUI();
        setupCoinFlipAnimation();
        setupFlipButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
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

    private void initializeViews() {
        coinHeadsImage = findViewById(R.id.imageHead);
        coinTailsImage = findViewById(R.id.imageTail);
        childrenSpinner = findViewById(R.id.spinnerChildren);
        coinFlipSuggestionText = findViewById(R.id.textFlipSuggestion);
        headsOrTailsGroup = findViewById(R.id.radioGroupHeadsOrTails);
        flipButton = findViewById(R.id.buttonFlip);
    }

    private void updateUI() {
        history.updateQueue(family.getChildrenInString());
        getCoinFlipRecommendation();
        populateChildrenSpinner();
    }

    private void getCoinFlipRecommendation() {
        coinFlipSuggestionText = findViewById(R.id.textFlipSuggestion);
        String recommendation = history.getNextInQueue();
        if (recommendation == HistoryManager.EMPTY) {
            coinFlipSuggestionText.setText(R.string.no_suggestion);
        }
        else {
            coinFlipSuggestionText.setText(getString(R.string.suggestion_header, recommendation));
        }
    }

    private void populateChildrenSpinner() {
        if (family.isNoChildren()) {
            childrenSpinner.setVisibility(View.GONE);
        }
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
        ObjectAnimator transitionUpHead = ObjectAnimator.ofFloat(coinHeadsImage, "translationY", ANIM_HEIGHT);
        ObjectAnimator transitionUpTail = ObjectAnimator.ofFloat(coinTailsImage, "translationY", ANIM_HEIGHT);
        transitionUpAnimation.playTogether(transitionUpHead, transitionUpTail);
        transitionUpAnimation.setInterpolator(new AnticipateOvershootInterpolator());

        ObjectAnimator transitionDownHead = ObjectAnimator.ofFloat(coinHeadsImage, "translationY", BASE_HEIGHT);
        ObjectAnimator transitionDownTail = ObjectAnimator.ofFloat(coinTailsImage, "translationY", BASE_HEIGHT);
        transitionDownAnimation.playTogether(transitionDownHead, transitionDownTail);
        transitionDownAnimation.setInterpolator(new BounceInterpolator());

        //Change camera distance for head and tail images
        coinHeadsImage.setCameraDistance(CAM_DISTANCE);
        coinTailsImage.setCameraDistance(CAM_DISTANCE);

        //Create coin flip sound
        coinFlipSound = MediaPlayer.create(this, R.raw.coinflipsound);

        coinFlipAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (isFlipping) {
                    updateCoinFlipAnimation();
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
        flipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewInteraction(false);
                int delay;

                if (random.nextBoolean()) {
                    delay = getDelay(EVEN_FLIPS_TIME);
                }
                else {
                    delay = getDelay(ODD_FLIP_TIME);
                }

                updateCoinFlipAnimation();
                startCoinFlipAnimation();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        coinFlipAnimation.cancel();
                        coinHeadsImage.setRotationX(0);
                        coinTailsImage.setRotationX(0);
                        setHeadsOrTails();
                        Toast.makeText(CoinFlipActivity.this, getResults(), Toast.LENGTH_SHORT).show();
                    }
                }, delay);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setViewInteraction(true);
                        if (!family.isNoChildren()) {
                            createHistoryEntry();
                            getCoinFlipRecommendation();
                        }
                    }
                }, delay + BOUNCE_INTERPOLATION_DELAY);
            }
        });
    }

    private void startCoinFlipAnimation() {
        coinFlipSound.start();
        transitionUpAnimation.start();
        transitionDownAnimation.start();
        coinFlipAnimation.start();
    }

    private int getDelay(int flipTime) {
        transitionUpAnimation.setDuration(flipTime / 2);
        transitionDownAnimation.setDuration((flipTime / 2) + BOUNCE_INTERPOLATION_DELAY);
        transitionDownAnimation.setStartDelay(flipTime / 2);
        int delay = flipTime;

        return delay;
    }

    private void updateCoinFlipAnimation() {
        if (isHead) {
            coinFlip1Animation.setTarget(coinHeadsImage);
            coinFlip2Animation.setTarget(coinTailsImage);
        }
        else {
            coinFlip1Animation.setTarget(coinTailsImage);
            coinFlip2Animation.setTarget(coinHeadsImage);
        }
    }

    private void setHeadsOrTails() {
        if (coinHeadsImage.getAlpha() == 1) {
            isHead = true;
        }
        if (coinTailsImage.getAlpha() == 1) {
            isHead = false;
        }
    }

    private void createHistoryEntry() {
        String name = childrenSpinner.getSelectedItem().toString();
        String choice = getChoice();
        HistoryEntry newEntry = new HistoryEntry(name, choice, getResults());
        history.addCoinFlipEntry(newEntry);
        history.queueRecentlyUsed(name);
    }

    private String getChoice() {
        int choiceID = headsOrTailsGroup.getCheckedRadioButtonId();
        RadioButton radioChoice = findViewById(choiceID);
        return (String) radioChoice.getText();
    }

    private String getResults() {
        if (isHead) {
            return HEADS;
        }
        return TAILS;
    }

    private void setViewInteraction(boolean isEnable) {
        childrenSpinner.setEnabled(isEnable);
        flipButton.setEnabled(isEnable);
        headsOrTailsGroup.setEnabled(isEnable);
        for (int i = 0; i < headsOrTailsGroup.getChildCount(); i++) {
            ((RadioButton) headsOrTailsGroup.getChildAt(i)).setEnabled(isEnable);
        }
    }

    public static Intent makeIntent(Context c) {
        Intent intent = new Intent(c, CoinFlipActivity.class);
        return intent;
    }
}
