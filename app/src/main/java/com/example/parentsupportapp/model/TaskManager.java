package com.example.parentsupportapp.model;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private static ArrayList<Task> taskArray;
    private static TaskManager instance;
    private static Context context;
    private static final String EMPTY_PREF = "";
    private static final String KEY_TASK = "TaskKey";
    private static final String PREF_TASK = "TaskPref";

    public static TaskManager getInstance(List<Child> children, Context context) {
        if (instance == null) {
            instance = new TaskManager(context);
        }

        updatePriorityQueues(children);
        return instance;
    }

    private TaskManager (Context context) {
        this.context = context;
        String jsonTaskManager = getTask(context);

        if (jsonTaskManager == EMPTY_PREF) {
            taskArray = new ArrayList<>();
        }
        else {
            taskArray = deserializeTaskArray(jsonTaskManager);
        }
    }

    public static void updatePriorityQueues(List<Child> children) {
        for (Task task: taskArray) {
            task.getPriorityQueue().updateQueue(children);
        }
    }

    public ArrayList<Task> getTaskArray() {
        return taskArray;
    }

    public void addTask (List<Child> children, String taskName) {
        this.taskArray.add(new Task(taskName, new PriorityQueue(children, EMPTY_PREF)));
        saveTaskSharedPrefs(context, this);
    }

    public void removeTask (int index) {
        this.taskArray.remove(index);
        saveTaskSharedPrefs(context, this);
    }

    public void editTask (int index, String name) {
        this.taskArray.get(index).setTaskName(name);
        saveTaskSharedPrefs(context, this);
    }

    private static void saveTaskSharedPrefs(Context context, TaskManager temp) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_TASK, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String jsonTask = gson.toJson(temp.getTaskArray());
        editor.putString(KEY_TASK, jsonTask);
        editor.apply();
    }

    public static String getTask(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_TASK, MODE_PRIVATE);
        return pref.getString(KEY_TASK, EMPTY_PREF);
    }

    private ArrayList<Task> deserializeTaskArray (String jsonTask) {
        return new Gson().fromJson(jsonTask, new TypeToken<Task>(){}.getType());
    }
}
