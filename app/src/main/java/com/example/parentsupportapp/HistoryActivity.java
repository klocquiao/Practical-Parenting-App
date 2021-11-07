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

import com.example.parentsupportapp.model.HistoryEntry;
import com.example.parentsupportapp.model.HistoryManager;
import com.google.gson.Gson;

public class HistoryActivity extends AppCompatActivity {
    private HistoryManager history;
    public static final String EMPTY_PREF = "";
    private static final String KEY_HISTORY = "HistoryKey";
    private static final String KEY_PRIORITY = "PriorityKey";
    private static final String PREF_HISTORY = "HistoryActivityPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        history = HistoryManager.getInstance(this);
        populateHistoryListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateHistoryListView();
    }

    public static void saveHistoryActivityPrefs(Context context, HistoryManager historyManager) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_HISTORY, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String jsonHistory = gson.toJson(historyManager.getHistory());
        String jsonPriority = gson.toJson(historyManager.getPriorityQueue());
        editor.putString(KEY_PRIORITY, jsonPriority);
        editor.putString(KEY_HISTORY, jsonHistory);
        editor.apply();
    }

    public static String getHistoryEntries(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_HISTORY, MODE_PRIVATE);
        return prefs.getString(KEY_HISTORY, EMPTY_PREF);
    }

    public static String getPriorityQueue(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_HISTORY, MODE_PRIVATE);
        return prefs.getString(KEY_PRIORITY, EMPTY_PREF);
    }

    private void populateHistoryListView() {
        ArrayAdapter<HistoryEntry> adapter = new HistoryListAdapter();
        ListView list = (ListView) findViewById(R.id.listHistory);
        list.setAdapter(adapter);
    }

    private class HistoryListAdapter extends ArrayAdapter<HistoryEntry> {
        public HistoryListAdapter() {
            super(HistoryActivity.this, R.layout.history_list_view, history.getHistory());
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View historyListView = convertView;
            if (historyListView == null) {
                historyListView = getLayoutInflater().inflate(R.layout.history_list_view, parent, false);
            }

            HistoryEntry currentHistoryEntry = history.getHistoryEntry(position);

            TextView textTimeOfFlip = (TextView) historyListView.findViewById(R.id.textTimeOfFlip);
            textTimeOfFlip.setText(currentHistoryEntry.getTimeOfFlip());

            TextView textFlipperName = (TextView) historyListView.findViewById(R.id.textFlipperName);
            textFlipperName.setText(currentHistoryEntry.getFlipperName());

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

            return historyListView;
        }
    }

    public static Intent makeIntent(Context c) {
        Intent intent = new Intent(c, HistoryActivity.class);
        return intent;
    }
}




