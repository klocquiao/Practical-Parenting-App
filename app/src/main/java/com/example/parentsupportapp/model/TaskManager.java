package com.example.parentsupportapp.model;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.parentsupportapp.TasksActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * The TaskManager manages a list of tasks. It is able to add tasks, remove tasks, edit tasks,
 * and get tasks in the list. It also handles the save of the Task list.
 */

public class TaskManager {
    private static ArrayList<Task> taskArray;
    private static TaskManager instance;
    private static Context context;

    public static final String EMPTY = "";

    public static TaskManager getInstance(List<Child> children, Context context) {
        if (instance == null) {
            instance = new TaskManager(context);
        }

        updatePriorityQueues(children);
        return instance;
    }

    private TaskManager (Context context) {
        this.context = context;
        String jsonTaskManager = TasksActivity.getTaskFromSharedPreferences(context);

        if (jsonTaskManager.matches(EMPTY)) {
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
        this.taskArray.add(new Task(taskName, children));
        TasksActivity.saveTaskSharedPrefs(context, this);
    }

    public void removeTask (int index) {
        this.taskArray.remove(index);
        TasksActivity.saveTaskSharedPrefs(context, this);
    }

    public Task getTask(int pos) {
        return taskArray.get(pos);
    }

    private ArrayList<Task> deserializeTaskArray (String jsonTask) {
        Type type = new TypeToken<List<Task>>(){}.getType();
        Gson gson = new Gson();
        return gson.fromJson(jsonTask, type);
    }
}
