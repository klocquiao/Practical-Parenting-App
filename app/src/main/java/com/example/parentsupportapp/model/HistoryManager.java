package com.example.parentsupportapp.model;

import java.util.ArrayList;
import java.util.List;

public class HistoryManager {
    private static List<HistoryEntry> history;

    public HistoryManager() {
        //Should be pulled from shared preferences
        history = new ArrayList<>();
    }

    public void addCoinFlipEntry(HistoryEntry newEntry) {
        //Prepend so recent history is at the top
        history.add(0, newEntry);
    }
}
