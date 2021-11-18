package com.example.parentsupportapp.model;

import android.content.Context;

import com.example.parentsupportapp.HistoryActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PriorityQueue {
    private List<Child> priorityQueue;
    public static final String EMPTY = "";

    public PriorityQueue(String jsonString) {
        if (jsonString.matches(EMPTY)) {
            priorityQueue = new ArrayList<>();
        }
        else {
            priorityQueue = deserializePriorityQueue(jsonString);
        }
    }

    public void updateQueue(List<Child> children) {
        for (Child child: children) {
            if (!priorityQueue.contains(child)) {
                priorityQueue.add(0, child);
            }
        }
        priorityQueue.removeIf(str -> !children.contains(str));
    }

    public void queueRecentlyUsed(Child child) {
        priorityQueue.remove(child);
        priorityQueue.add(child);
    }

    public String getNextInQueue() {
        if (priorityQueue.isEmpty()) {
            return EMPTY;
        }
        return priorityQueue.get(0).toString();
    }

    public List<Child> getPriorityQueue() {
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

    public void remove(Child child) {
        priorityQueue.remove(child);
    }
    public Child getChild(int pos) {
        return priorityQueue.get(pos);
    }


    private List<Child> deserializePriorityQueue(String jsonPriority) {
        Type type = new TypeToken<List<Child>>(){}.getType();
        Gson gson = new Gson();
        return gson.fromJson(jsonPriority, type);
    }
}
