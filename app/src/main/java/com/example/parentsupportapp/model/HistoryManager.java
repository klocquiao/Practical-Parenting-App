package com.example.parentsupportapp.model;

import android.content.Context;

import com.example.parentsupportapp.HistoryActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HistoryManager {
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
        String jsonHistory = HistoryActivity.getHistory(context);
        if (jsonHistory == HistoryActivity.EMPTY_HISTORY) {
            history = new ArrayList<>();
        }
        else {
            history = deserializeHistory(jsonHistory);
        }
    }

    public void addCoinFlipEntry(HistoryEntry newEntry) {
        history.add(0, newEntry);
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
