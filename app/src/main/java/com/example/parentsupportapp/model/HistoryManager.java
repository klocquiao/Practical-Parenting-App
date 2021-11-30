package com.example.parentsupportapp.model;

import android.content.Context;

import com.example.parentsupportapp.HistoryActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * HistoryManager manages coin flip history entries and the priority queue.
 * It allows a new entry to be added to a list of history entries whenever the correlated function is called.]
 * It also keeps track of who has and who hasn't flipped recently.
 * It calls HistoryActivity sharedPrefs whenever either the priority queue or the list of history entries is modified.
 */

public class HistoryManager {
    public static final String EMPTY = "";
    private List<HistoryEntry> history;

    public HistoryManager(List<Child> children, String jsonHistory) {
        if (jsonHistory.matches(EMPTY)) {
            history = new ArrayList<>();
        }
        else {
            history = deserializeHistory(jsonHistory);
        }

        updateChildObjects(children);
    }

    private void updateChildObjects(List<Child> children) {
        for (HistoryEntry entry: history) {
            int index = children.indexOf(entry.getChild());
            if (index != -1) {
                entry.setChild(children.get(index));
            }
        }
    }

    public void addCoinFlipEntry(HistoryEntry newEntry) {
        history.add(0, newEntry);
    }

    public List<HistoryEntry> getHistory() {
        return history;
    }

    public static List<HistoryEntry> deserializeHistory(String jsonHistory) {
        Type type = new TypeToken<List<HistoryEntry>>(){}.getType();
        Gson gson = new Gson();
        return gson.fromJson(jsonHistory, type);
    }
}
