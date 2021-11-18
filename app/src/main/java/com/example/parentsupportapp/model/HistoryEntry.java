/**
 * HistoryEntry contains data concerning coin flips.
 * This includes the time of flip, the flip name, the flip choice, and the results of the flip.
 * These entries are to be stored in a list of entries that exists in a history manager object.
 */

package com.example.parentsupportapp.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoryEntry {
    private Child flipperChild;
    private String timeOfFlip;
    private String flipChoice;
    private String flipResult;

    public HistoryEntry(Child flipperChild, String flipChoice, String flipResult) {
        this.timeOfFlip = convertTimeToString();
        this.flipperChild = flipperChild;
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
        return flipperChild.toString();
    }
    public String getFlipperImage() {
        return flipperChild.getPortraitPath();
    }

}
