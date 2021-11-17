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
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.example.parentsupportapp.model.PriorityQueue;
import com.google.gson.Gson;

import java.util.Random;

public class CoinFlipActivity extends AppCompatActivity {
    private static final String KEY_PRIORITY = "CoinFlipPriorityKey";
    private static final String PREF_PRIORITY = "CoinFlipActivityPref";

    private static final String HEADS = "Heads";
    private static final String TAILS = "Tails";
    private static final String NOBODY = "Nobody";

    public static final float BASE_HEIGHT = 0f;
    public static final float ANIM_HEIGHT = -250f;
    public static final int TRANSITION_TIME = 1300;
    public static final int BOUNCE_INTERPOLATION_DELAY = 400;
    public static final int CAM_DISTANCE = 8000;
    public static final int EVEN_NUM_FLIPS = 6;
    public static final int ODD_NUM_FLIPS = 7;

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

    private int numFlips;
    private int delay;

    private Family family;
    private HistoryManager history;
    private PriorityQueue coinFlipPriorityQueue;

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
        coinFlipPriorityQueue = new PriorityQueue(getPriorityQueue(this));
        coinFlipPriorityQueue.updateQueue(family.getChildrenInString());

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
    protected void onStop() {
        super.onStop();
        coinFlipPriorityQueue.remove(NOBODY);
        saveHistoryActivityPrefs(this);
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
        getCoinFlipRecommendation();
        populateChildrenSpinner();
    }

    private void getCoinFlipRecommendation() {
        String recommendation = coinFlipPriorityQueue.getNextInQueue();
        if (recommendation.matches(HistoryManager.EMPTY)) {
            coinFlipSuggestionText.setText(R.string.no_suggestion);
        }
        else {
            coinFlipSuggestionText.setText(getString(R.string.suggestion_header, recommendation));
        }
    }

    private void populateChildrenSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, coinFlipPriorityQueue.getPriorityQueue());
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        childrenSpinner.setAdapter(adapter);
        adapter.remove(NOBODY);
        if (family.isNoChildren()) {
            childrenSpinner.setVisibility(View.GONE);
        }
        else {
            adapter.add(NOBODY);
        }
    }

    private void setupCoinFlipAnimation() {
        coinFlipAnimation = new AnimatorSet();
        transitionUpAnimation = new AnimatorSet();
        transitionDownAnimation = new AnimatorSet();

        coinFlip1Animation = AnimatorInflater.loadAnimator(this,
                R.animator.coin_flip_animator_1);
        coinFlip2Animation = AnimatorInflater.loadAnimator(this,
                R.animator.coin_flip_animator_2);
        coinFlipAnimation.playTogether(coinFlip1Animation, coinFlip2Animation);

        setupUpDownAnimation();

        float scale = CoinFlipActivity.this.getResources().getDisplayMetrics().density;
        coinHeadsImage.setCameraDistance(CAM_DISTANCE * scale);
        coinTailsImage.setCameraDistance(CAM_DISTANCE * scale);

        coinFlipSound = MediaPlayer.create(this, R.raw.coinflipsound);

        coinFlipAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (numFlips == delay) {
                    animation.cancel();

                }
                else {
                    updateCoinFlip();
                    coinFlipAnimation.start();
                }
            }

            public void onAnimationStart(Animator animation) {
                numFlips++;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                coinHeadsImage.setRotationX(0);
                coinTailsImage.setRotationX(0);
            }
        });
    }

    private void setupUpDownAnimation() {
        ObjectAnimator transitionUpHead = ObjectAnimator.ofFloat(coinHeadsImage, "translationY", ANIM_HEIGHT);
        ObjectAnimator transitionUpTail = ObjectAnimator.ofFloat(coinTailsImage, "translationY", ANIM_HEIGHT);
        transitionUpAnimation.playTogether(transitionUpHead, transitionUpTail);
        transitionUpAnimation.setInterpolator(new AnticipateOvershootInterpolator());
        transitionUpAnimation.setDuration(TRANSITION_TIME / 2);

        ObjectAnimator transitionDownHead = ObjectAnimator.ofFloat(coinHeadsImage, "translationY", BASE_HEIGHT);
        ObjectAnimator transitionDownTail = ObjectAnimator.ofFloat(coinTailsImage, "translationY", BASE_HEIGHT);
        transitionDownAnimation.playTogether(transitionDownHead, transitionDownTail);
        transitionDownAnimation.setInterpolator(new BounceInterpolator());
        transitionDownAnimation.setDuration((TRANSITION_TIME / 2) + BOUNCE_INTERPOLATION_DELAY);
        transitionDownAnimation.setStartDelay(TRANSITION_TIME / 2);
    }

    private void setupFlipButton() {
        flipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewInteraction(false);

                numFlips = 0;
                if (random.nextBoolean()) {
                    delay = EVEN_NUM_FLIPS;
                }
                else {
                    delay = ODD_NUM_FLIPS;
                }

                updateCoinFlip();
                startCoinFlipAnimation();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CoinFlipActivity.this, getResults(), Toast.LENGTH_SHORT).show();
                        setViewInteraction(true);
                        String name = childrenSpinner.getSelectedItem().toString();
                        if (!coinFlipPriorityQueue.isEmpty() && !name.matches(NOBODY)) {
                            createHistoryEntry();
                            updateUI();
                        }
                    }
                }, 2000);
            }
        });
    }

    private void startCoinFlipAnimation() {
        coinFlipSound.start();
        transitionUpAnimation.start();
        transitionDownAnimation.start();
        coinFlipAnimation.start();
    }

    private void updateCoinFlip() {
        if (isHead) {
            coinFlip1Animation.setTarget(coinHeadsImage);
            coinFlip2Animation.setTarget(coinTailsImage);
            isHead = false;
        }
        else {
            coinFlip1Animation.setTarget(coinTailsImage);
            coinFlip2Animation.setTarget(coinHeadsImage);
            isHead = true;
        }
        coinFlipAnimation.playTogether(coinFlip1Animation, coinFlip2Animation);
    }

    private void createHistoryEntry() {
        String name = childrenSpinner.getSelectedItem().toString();
        String choice = getChoice();
        HistoryEntry newEntry = new HistoryEntry(name, choice, getResults());
        history.addCoinFlipEntry(newEntry);
        coinFlipPriorityQueue.queueRecentlyUsed(name);
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
            (headsOrTailsGroup.getChildAt(i)).setEnabled(isEnable);
        }
    }

    public static Intent makeIntent(Context c) {
        Intent intent = new Intent(c, CoinFlipActivity.class);
        return intent;
    }

    public static String getPriorityQueue(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_PRIORITY, MODE_PRIVATE);
        return prefs.getString(KEY_PRIORITY, PriorityQueue.EMPTY);
    }

    private void saveHistoryActivityPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_PRIORITY, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String jsonPriority = gson.toJson(coinFlipPriorityQueue.getPriorityQueue());
        editor.putString(KEY_PRIORITY, jsonPriority);
        editor.apply();
    }
}
