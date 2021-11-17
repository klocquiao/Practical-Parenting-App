package com.example.parentsupportapp.model;

import java.util.ArrayList;

public class TaskManager {

    private ArrayList<Task> taskArray;
    private static TaskManager instance;

    private TaskManager () {}
    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    public void addTask (String name) {
        this.taskArray.add(new Task(name));
    }

    public void removeTask (int index) {

    }

    public void editTask (int index, String name) {
        this.taskArray.get(index).setName(name);
    }

    public boolean isTask () {
        return taskArray.isEmpty();
    }

}
