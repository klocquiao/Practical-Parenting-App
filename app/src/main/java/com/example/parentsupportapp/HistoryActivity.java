package com.example.parentsupportapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.parentsupportapp.childConfig.ViewActivity;
import com.example.parentsupportapp.model.Family;
import com.example.parentsupportapp.model.HistoryEntry;
import com.example.parentsupportapp.model.HistoryManager;
import com.google.gson.Gson;

import java.util.List;

/**
 * HistoryActivity presents the list of coin flip entries to the user, taking said data
 * from the HistoryManager. HistoryActivity also contains the sharedPreferences used by
 * the HistoryManager to save the history entry list and priority queue after the application is
 * terminated.
 */

public class HistoryActivity extends AppCompatActivity {
    private static final String EXTRA_JSON_HISTORY = "com.example.parentsupportapp.HistoryActivity.json_history";
    private static final String EXTRA_IS_TASK = "com.example.parentsupportapp.HistoryActivity.is_task";

    private List<HistoryEntry> history;
    private Boolean isTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        isTask = getIntent().getBooleanExtra(EXTRA_IS_TASK, false);
        String jsonHistory = getIntent().getStringExtra(EXTRA_JSON_HISTORY);

        if (!jsonHistory.matches(HistoryManager.EMPTY)) {
            history = HistoryManager.deserializeHistory(jsonHistory);
            populateHistoryListView();
        }
    }

    private void populateHistoryListView() {
        ArrayAdapter<HistoryEntry> adapter = new HistoryListAdapter();
        ListView list = findViewById(R.id.listHistory);
        list.setAdapter(adapter);
    }

    private class HistoryListAdapter extends ArrayAdapter<HistoryEntry> {
        public HistoryListAdapter() {
            super(HistoryActivity.this, R.layout.history_list_view, history);
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View historyListView = convertView;
            if (historyListView == null) {
                historyListView = getLayoutInflater().inflate(R.layout.history_list_view, parent, false);
            }

            HistoryEntry currentHistoryEntry = history.get(position);

            TextView textTimeOfFlip = (TextView) historyListView.findViewById(R.id.textTimeOfFlip);
            textTimeOfFlip.setText(currentHistoryEntry.getTimeOfFlip());

            TextView textFlipperName = (TextView) historyListView.findViewById(R.id.textFlipperName);
            textFlipperName.setText(currentHistoryEntry.getChildName());

            TextView textFlipChoice = (TextView) historyListView.findViewById(R.id.textFlipChoice);
            textFlipChoice.setText(getString(R.string.history_choice,currentHistoryEntry.getFlipChoice()));

            TextView textFlipResult = (TextView) historyListView.findViewById(R.id.textFlipResult);
            textFlipResult.setText(getString(R.string.history_results, currentHistoryEntry.getFlipResult()));

            ImageView imageIsMatch = (ImageView) historyListView.findViewById(R.id.imageIsMatch);
            if (currentHistoryEntry.isMatch()) {
                imageIsMatch.setImageResource(R.drawable.ic_baseline_check_circle_outline);
            }
            else {
                imageIsMatch.setImageResource(R.drawable.ic_baseline_x_circle);
            }

            ImageView imageChild = (ImageView) historyListView.findViewById(R.id.imagePastFlipper);
            ViewActivity.loadImageFromStorage(currentHistoryEntry.getChildImage(), imageChild, HistoryActivity.this);


            return historyListView;
        }
    }

    public static Intent makeIntent(Context c, String jsonHistory, Boolean isTask) {
        Intent intent = new Intent(c, HistoryActivity.class);
        intent.putExtra(EXTRA_JSON_HISTORY, jsonHistory);
        intent.putExtra(EXTRA_IS_TASK, isTask);
        return intent;
    }
}




