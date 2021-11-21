package com.example.parentsupportapp.model;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private ArrayList<Task> taskArray;
    private static TaskManager instance;
    private Context context;
    private static final String EMPTY_PREF = "";
    private static final String KEY_Task = "TaskKey";
    private static final String PREF_TASK = "TaskPref";

    private TaskManager (Context context) {
        this.context = context;
        String jsonTaskManager = getTask(context);
        if(jsonTaskManager == EMPTY_PREF) {
            taskArray = new ArrayList<>();
        }
        else {
            taskArray = deserializeTaskArray(jsonTaskManager);
        }
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
        saveTaskSharedPrefs(context, this);
    }

    public void removeTask (int index) {
        this.taskArray.remove(index);
        saveTaskSharedPrefs(context, this);
    }

    public void editTask (int index, String name) {
        this.taskArray.get(index).setName(name);
        saveTaskSharedPrefs(context, this);
    }

    private static void saveTaskSharedPrefs(Context context, TaskManager temp) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_TASK, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String jsonTask = gson.toJson(temp.getTaskArray());
        editor.putString(KEY_Task, jsonTask);
        editor.apply();
    }

    public static String getTask(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_TASK, MODE_PRIVATE);
        return pref.getString(KEY_Task, EMPTY_PREF);
    }

    private ArrayList<Task> deserializeTaskArray (String jsonTask) {
        return new Gson().fromJson(jsonTask, new TypeToken<Task>(){}.getType());
    }

}
