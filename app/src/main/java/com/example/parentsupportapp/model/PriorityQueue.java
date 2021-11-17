package com.example.parentsupportapp.model;

import android.content.Context;

import com.example.parentsupportapp.HistoryActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PriorityQueue {
    private List<String> priorityQueue;
    public static final String EMPTY = "";

    public PriorityQueue(String jsonString) {
        if (jsonString.matches(EMPTY)) {
            priorityQueue = new ArrayList<>();
        }
        else {
            priorityQueue = deserializePriorityQueue(jsonString);
        }
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
    }

    public void queueRecentlyUsed(String childName) {
        priorityQueue.remove(childName);
        priorityQueue.add(childName);
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

    public boolean isEmpty() {
        if (priorityQueue.size() == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    private List<String> deserializePriorityQueue(String jsonPriority) {
        Type type = new TypeToken<List<String>>(){}.getType();
        Gson gson = new Gson();
        return gson.fromJson(jsonPriority, type);
    }
}
