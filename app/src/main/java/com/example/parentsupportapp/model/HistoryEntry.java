package com.example.parentsupportapp.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoryEntry {
    private String flipperName;
    private String timeOfFlip;
    private int flipChoice;
    private int flipResult;

    public HistoryEntry(String flipperName, int flipChoice, int flipResult) {
        this.timeOfFlip = convertTimeToString();
        this.flipperName = flipperName;
        this.flipChoice = flipChoice;
        this.flipResult = flipResult;
    }

    public boolean isMatch() {
        if (flipChoice == flipResult) {
            return true;
        }
        return false;
    }

    private String convertTimeToString() {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d @ hh:mm");
        String strTimeOfFlip = time.format(formatter);
        return strTimeOfFlip;
    }

    public String getTimeOfFlip() {
        return timeOfFlip;
    }

    public int getFlipChoice() {
        return flipChoice;
    }

    public int getFlipResult() {
        return flipResult;
    }

    public String getFlipperName() {
        return flipperName;
    }
}
