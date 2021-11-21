package com.example.parentsupportapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.parentsupportapp.model.Child;
import com.example.parentsupportapp.model.Family;
import com.example.parentsupportapp.tasksConfig.AddTaskActivity;
import com.example.parentsupportapp.tasksConfig.EditTaskActivity;
import com.example.parentsupportapp.tasksConfig.RemoveTaskActivity;

import java.util.List;

public class TasksActivity extends AppCompatActivity {

    private Family fam;
    private Button addTaskButton;
    private Button editTaskButton;
    private Button removeTaskButton;
    private ListView listView;
    private List<Child> children;

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

    private void populateListView() {
        // array adapter
        ArrayAdapter<Child> adapter = new TaskListAdapter();
        listView.setAdapter(adapter);
    }

    // TODO: possibly refactor out this entire class (will need to use it in another activity)
    private class TaskListAdapter extends ArrayAdapter<Child> {
        public TaskListAdapter() {
            super(TasksActivity.this, R.layout.task_item_view, children);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.task_item_view, parent, false);
            }

            // TODO: Replace the child with the appropriate task
            Child currChild = children.get(position);
            // fill the view
            ImageView imageView = (ImageView)itemView.findViewById(R.id.imageViewTaskItem);

            Bitmap bitmap = BitmapFactory.decodeFile(currChild.getPortraitPath());
            if (bitmap == null) {
                imageView.setImageResource(R.drawable.ic_baseline_broken_image_24);
            } else {
                imageView.setImageBitmap(bitmap);
            }

            TextView textView = (TextView) itemView.findViewById(R.id.textViewTaskItem);
            textView.setText(currChild.getFirstName());

            return itemView;
        }
    }

    private void initializeFields() {
        addTaskButton = findViewById(R.id.buttonAddTask);
        editTaskButton = findViewById(R.id.buttonEditTask);
        removeTaskButton = findViewById(R.id.buttonRemoveTask);
        fam = Family.getInstance(this);
        listView = findViewById(R.id.listViewTasks);
        children = fam.getChildren();
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
                // NOTE: position gives position of thing clicked.
                // TODO: have to implement the functionality when an item in the list view is clicked.

            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TasksActivity.class);
    }
}