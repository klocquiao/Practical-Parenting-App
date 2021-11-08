package com.example.parentsupportapp.childConfig;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.parentsupportapp.R;
import com.example.parentsupportapp.model.Family;

public class RemoveActivity extends AppCompatActivity {

    private ListView listView;
    private Family fam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove);

        fam = Family.getInstance(this);
        updateUI();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fam.removeChild(position);
                updateUI();
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, RemoveActivity.class);
    }

    private void updateUI() {
        listView = findViewById(R.id.listChildrenToRemove);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RemoveActivity.this, android.R.layout.simple_list_item_1,fam.getChildrenInString());
        listView.setAdapter(arrayAdapter);
    }
}