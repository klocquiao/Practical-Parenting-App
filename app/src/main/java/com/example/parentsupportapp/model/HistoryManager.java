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
    private List<String> priorityQueue;
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
        String jsonPriorityQueue = HistoryActivity.getPriorityQueue(context);

        if (jsonHistory == HistoryActivity.EMPTY_PREF) {
            history = new ArrayList<>();
        }
        else {
            history = deserializeHistory(jsonHistory);
        }
        if (jsonPriorityQueue == HistoryActivity.EMPTY_PREF) {
            priorityQueue = new ArrayList<>();
        }
        else {
            priorityQueue = deserializePriorityQueue(jsonPriorityQueue);
        }
    }

    public void addCoinFlipEntry(HistoryEntry newEntry) {
        history.add(0, newEntry);
        HistoryActivity.saveHistoryActivityPrefs(context, this);
    }

    public void updateQueue(List<String> strChildren) {
        for (int i = 0; i < strChildren.size(); i++) {
            if (!priorityQueue.contains(strChildren.get(i))) {
                priorityQueue.add(0, strChildren.get(i));
            }
        }
        for (int j = 0; j < priorityQueue.size(); j++) {
            if (!strChildren.contains(priorityQueue.get(j))) {
                priorityQueue.remove(j);
            }
        }

        HistoryActivity.saveHistoryActivityPrefs(context, this);
    }

    public void queueRecentlyUsed(String childName) {
        priorityQueue.remove(childName);
        priorityQueue.add(childName);
        HistoryActivity.saveHistoryActivityPrefs(context, this);
    }

    public String getNextInQueue() {
        if (priorityQueue.isEmpty()) {
            return EMPTY;
        }
        return priorityQueue.get(0);
    }

    public List<String> getPriorityQueue() {
        return priorityQueue;
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

    private List<String> deserializePriorityQueue(String jsonPriority) {
        Type type = new TypeToken<List<String>>(){}.getType();
        Gson gson = new Gson();
        return gson.fromJson(jsonPriority, type);
    }
}
