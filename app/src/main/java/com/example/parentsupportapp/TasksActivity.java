package com.example.parentsupportapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.parentsupportapp.childConfig.ViewActivity;
import com.example.parentsupportapp.model.Family;
import com.example.parentsupportapp.model.Task;
import com.example.parentsupportapp.model.TaskManager;
import com.example.parentsupportapp.tasksConfig.AddTaskActivity;
import com.example.parentsupportapp.tasksConfig.EditTaskActivity;
import com.example.parentsupportapp.tasksConfig.RemoveTaskActivity;
import com.example.parentsupportapp.tasksConfig.ViewTaskActivity;
import com.google.gson.Gson;

public class TasksActivity extends AppCompatActivity {

    public static final String TASK_POSITION = "clickedTaskPosition";
    private Family family;
    private TaskManager taskManager;
    private Button addTaskButton;
    private Button editTaskButton;
    private Button removeTaskButton;
    private ListView listView;

    private static final String KEY_TASK = "TaskKey";
    private static final String PREF_TASK = "TaskPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        Toolbar toolbar = findViewById(R.id.toolbarTasks);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle(R.string.tasks_title);
        }

        initializeFields();
        setupButtonListeners();
        
        populateListView();
        registerClickCallback();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateListView();
        registerClickCallback();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveTaskSharedPrefs(this, taskManager);
    }

    private void populateListView() {
        // array adapter
        ArrayAdapter<Task> adapter = new TaskListAdapter();
        listView.setAdapter(adapter);
    }

    private void initializeFields() {
        family = Family.getInstance(this);
        taskManager = TaskManager.getInstance(family.getChildren(), this);
        addTaskButton = findViewById(R.id.buttonAddTask);
        editTaskButton = findViewById(R.id.buttonEditTask);
        removeTaskButton = findViewById(R.id.buttonRemoveTask);
        listView = findViewById(R.id.listViewTasks);
    }

    private void setupButtonListeners() {
        setOnClickBehaviour(addTaskButton, AddTaskActivity.makeIntent(this));
        setOnClickBehaviour(editTaskButton, EditTaskActivity.makeIntent(this));
        setOnClickBehaviour(removeTaskButton, RemoveTaskActivity.makeIntent(this));
    }

    private void setOnClickBehaviour(Button button, Intent intent) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }

    private void registerClickCallback() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Intent viewTaskIntent = ViewTaskActivity.makeIntent(TasksActivity.this);
                viewTaskIntent.putExtra(TASK_POSITION, position);
                startActivity(viewTaskIntent);

            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TasksActivity.class);
    }

    public static void saveTaskSharedPrefs(Context context, TaskManager temp) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_TASK, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String jsonTask = gson.toJson(temp.getTaskArray());
        editor.putString(KEY_TASK, jsonTask);
        editor.apply();
    }

    public static String getTaskFromSharedPreferences(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_TASK, MODE_PRIVATE);
        return pref.getString(KEY_TASK, TaskManager.EMPTY);
    }

    private class TaskListAdapter extends ArrayAdapter<Task> {
        public TaskListAdapter() {
            super(TasksActivity.this, R.layout.task_item_adapter_view, taskManager.getTaskArray());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.task_item_adapter_view, parent, false);
            }
            Task currentTask = taskManager.getTask(position);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewTaskItem);
            ViewActivity.loadImageFromStorage(currentTask.getNextChildInQueueImage(), imageView, TasksActivity.this);

            TextView textViewTask = (TextView) itemView.findViewById(R.id.textViewTaskItem);
            textViewTask.setText(currentTask.getTaskName());

            TextView textViewChildName = (TextView) itemView.findViewById(R.id.textViewTaskChildName);
            textViewChildName.setText(currentTask.getNextChildInQueueName());

            return itemView;
        }
    }

}