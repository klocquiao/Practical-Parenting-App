package com.example.parentsupportapp.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoryEntry {
    private static final int HEADS = 0;
    private static final int TAILS = 1;
    private int flipperName;
    private LocalDateTime timeOfFlip;
    private int flipChoice;
    private int flipResult;

    public HistoryEntry(int flipperName, int flipChoice, int flipResult) {
        this.timeOfFlip = LocalDateTime.now();
        this.flipperName = flipperName;
        this.flipChoice = flipChoice;
        this.flipResult = flipResult;
    }

    public String getTimeOfFlip() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d @ hh:mm");
        String gameCompletionTimeString = timeOfFlip.format(formatter);
        return gameCompletionTimeString;
    }

    public int getFlipChoice() {
        return flipChoice;
    }

    public int getFlipResult() {
        return flipResult;
    }
}
