package com.example.parentsupportapp.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * HistoryEntry contains data concerning coin flips.
 * This includes the time of flip, the flip name, the flip choice, and the results of the flip.
 * These entries are to be stored in a list of entries that exists in a history manager object.
 */

public class HistoryEntry {
    private Child child;
    private String timeOfFlip;
    private String flipChoice;
    private String flipResult;

    public HistoryEntry(Child child) {
        this.timeOfFlip = convertTimeToString();
        this.child = child;
    }

    public HistoryEntry(Child child, String flipChoice, String flipResult) {
        this.timeOfFlip = convertTimeToString();
        this.child = child;
        this.flipChoice = flipChoice;
        this.flipResult = flipResult;
    }

    private String convertTimeToString() {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d @ hh:mm");
        String strTimeOfFlip = time.format(formatter);
        return strTimeOfFlip;
    }

    public boolean isMatch() {
        if (flipChoice.matches(flipResult)) {
            return true;
        }
        return false;
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

    public Child getChild() {
        return child;
    }

    public String getChildName() {
        return child.toString();
    }

    public String getChildImage() {
        return child.getPortraitPath();
    }

    public void setChild(Child child) {
        this.child = child;
    }
}
