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

    public PriorityQueue(List<Child> children, String jsonString) {
        if (jsonString.matches(EMPTY)) {
            priorityQueue = new ArrayList<>();
        }
        else {
            priorityQueue = deserializePriorityQueue(jsonString);
        }
        updateQueue(children);
    }

    public void updateQueue(List<Child> children) {
        for (Child child: children) {
            if (!priorityQueue.contains(child)) {
                priorityQueue.add(0, child);
            }
        }
        priorityQueue.removeIf(child -> !children.contains(child));
        updateChildObjects(children);
    }

    public void updateChildObjects(List<Child> children) {
        for (int i = 0; i < priorityQueue.size(); i++) {
            int index = children.indexOf(priorityQueue.get(i));
            if (index != -1) {
                priorityQueue.set(i, children.get(index));
            }
        }
    }

    public void queueRecentlyUsed(Child child) {
        priorityQueue.remove(child);
        priorityQueue.add(child);
    }

    public List<Child> getPriorityQueue() {
        return priorityQueue;
    }

    public Child getNextInQueue() {
        if (priorityQueue.isEmpty()) {
            return new Child("Nobody");
        }
        return priorityQueue.get(0);
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
