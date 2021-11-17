package com.example.parentsupportapp.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private ArrayList<Task> taskArray;
    private static TaskManager instance;
    private static Context context;

    private TaskManager (Context context) {
        TaskManager.context = context;
        //String jsonTaskManager = TaskActivity
    }

    public static TaskManager getInstance(Context context) {
        if (instance == null) {
            instance = new TaskManager(context);
        }
        return instance;
    }

    public ArrayList<Task> getTaskArray() {
        return taskArray;
    }

    public List<String> getTaskAsString() {
        List<String> arr = new ArrayList<>();
        for(int i = 0 ; i < this.taskArray.size() ; i++) {
            arr.add(this.taskArray.get(i).getName());
        }
        return arr;
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
