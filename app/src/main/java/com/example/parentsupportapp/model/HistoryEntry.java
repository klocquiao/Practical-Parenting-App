package com.example.parentsupportapp.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoryEntry {
    private String flipperName;
    private String timeOfFlip;
    private String flipChoice;
    private String flipResult;

    public HistoryEntry(String flipperName, String flipChoice, String flipResult) {
        this.timeOfFlip = convertTimeToString();
        this.flipperName = flipperName;
        this.flipChoice = flipChoice;
        this.flipResult = flipResult;
    }

    public boolean isMatch() {
        if (flipChoice.matches(flipResult)) {
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

    public String getFlipChoice() {
        return flipChoice;
    }

    public String getFlipResult() {
        return flipResult;
    }

    public String getFlipperName() {
        return flipperName;
    }
}
