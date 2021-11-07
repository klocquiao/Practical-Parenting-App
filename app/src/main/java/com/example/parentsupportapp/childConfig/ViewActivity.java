package com.example.parentsupportapp.childConfig;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.parentsupportapp.R;
import com.example.parentsupportapp.model.Family;

public class ViewActivity extends AppCompatActivity {

    private ListView listView;
    private Family fam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        setupBackButton();
        fam = Family.getInstance(this);
        listView = findViewById(R.id.listChildren);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ViewActivity.this, android.R.layout.simple_list_item_1,fam.getChildrenInString());
        listView.setAdapter(arrayAdapter);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ViewActivity.class);
    }

    private void setupBackButton() {
        Button btn = findViewById(R.id.btnBackFromView);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}