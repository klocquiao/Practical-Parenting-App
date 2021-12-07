package com.example.parentsupportapp.childConfigActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.parentsupportapp.R;
import com.example.parentsupportapp.model.Child;
import com.example.parentsupportapp.model.Family;
import com.example.parentsupportapp.model.SaveImage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.List;

/**
 * View activity helps to view all the children that have been added to the app.
 * In this activity the user can click the floating action button to add a child to the list of saved
 * children or click one of the children from the list which allows them to edit/remove said kid.
 */

public class ViewActivity extends AppCompatActivity {
    public static final String EMPTY_PREF = "";
    private static final String KEY_FAMILY = "FamilyKey";
    private static final String PREF_CHILD_CONFIG = "ChildConfigPref";
    public static final String EXTRA_POSITION = "intVariableName";
    private ListView childrenListView;
    private Family family;
    private List<Child> children;
    private FloatingActionButton fabAddChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Toolbar toolbar = findViewById(R.id.tbView);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        family = Family.getInstance(this);
        children = family.getChildren();
        childrenListView = findViewById(R.id.listViewChildren);

        populateListView();
        registerClickCallback();
        setupFabAddButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateListView();
        registerClickCallback();
    }

    public void populateListView() {
        ArrayAdapter<Child> adapter = new MyListAdapter();
        ListView listViewChildren = findViewById(R.id.listViewChildren);
        listViewChildren.setAdapter(adapter);
    }

    private void registerClickCallback() {
        ListView listViewChildren = findViewById(R.id.listViewChildren);
        listViewChildren.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(ViewActivity.this, EditRemoveChildActivity.class);
                myIntent.putExtra(EXTRA_POSITION, position);
                startActivity(myIntent);
                populateListView();
            }
        });
    }

    private void setupFabAddButton() {
        fabAddChild = findViewById(R.id.fabAddChild);
        fabAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddChildActivity.makeIntent(ViewActivity.this);
                startActivity(intent);
                populateListView();
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ViewActivity.class);
    }

    public static void loadImageFromStorage(String path, ImageView img, Context context) {
        Bitmap bitmap = new SaveImage(context).
                setFileName(path).
                load();
        img.setImageBitmap(bitmap);
    }

    private class MyListAdapter extends ArrayAdapter<Child> {
        public MyListAdapter() {
            super(ViewActivity.this, R.layout.child_view_layout, family.getChildren());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.child_view_layout, parent, false);
            }

            Child currentChild = children.get(position);

            ImageView img = itemView.findViewById(R.id.imgViewChild);
            loadImageFromStorage(currentChild.getPortraitPath(),img,ViewActivity.this);

            TextView textView = itemView.findViewById(R.id.textViewName);
            textView.setText(currentChild.getFirstName());

            return itemView;
        }
    }

    public static void saveChildConfigPrefs(Context context, Family family) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_CHILD_CONFIG, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String jsonFamily = gson.toJson(family.getChildren());
        editor.putString(KEY_FAMILY, jsonFamily);
        editor.apply();
    }

    public static String getFamily(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_CHILD_CONFIG, MODE_PRIVATE);
        return prefs.getString(KEY_FAMILY, EMPTY_PREF);
    }
}