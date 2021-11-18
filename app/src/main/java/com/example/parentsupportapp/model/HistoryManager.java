/**
 * HistoryManager manages coin flip history entries and the priority queue.
 * It allows a new entry to be added to a list of history entries whenever the correlated function is called.]
 * It also keeps track of who has and who hasn't flipped recently.
 * It calls HistoryActivity sharedPrefs whenever either the priority queue or the list of history entries is modified.
 */

package com.example.parentsupportapp.model;

import android.content.Context;
import android.util.Log;

import com.example.parentsupportapp.HistoryActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HistoryManager {
    public static final String EMPTY = "";
    private List<HistoryEntry> history;
    private static HistoryManager instance;
    private static Context context;

    public static HistoryManager getInstance(Context context) {
        if (instance == null) {
            instance = new HistoryManager(context);
        }
        return instance;
    }

    public HistoryManager(Context context) {
        this.context = context;
        String jsonHistory = HistoryActivity.getHistoryEntries(context);

        if (jsonHistory == EMPTY) {
            history = new ArrayList<>();
        }
        else {
            history = deserializeHistory(jsonHistory);
        }
    }

    public void addCoinFlipEntry(HistoryEntry newEntry) {
        history.add(0, newEntry);
        HistoryActivity.saveHistoryActivityPrefs(context, this);
    }

    public List<HistoryEntry> getHistory() {
        return history;
    }

    public HistoryEntry getHistoryEntry(int position) {
       return history.get(position);
    }

    private List<HistoryEntry> deserializeHistory(String jsonHistory) {
        Type type = new TypeToken<List<HistoryEntry>>(){}.getType();
        Gson gson = new Gson();
        return gson.fromJson(jsonHistory, type);
    }
}
