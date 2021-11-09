package com.example.parentsupportapp.childConfig;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

        Toolbar toolbar = findViewById(R.id.tbRem);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        fam = Family.getInstance(this);
        updateUI();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //fam.removeChild(position);


                View dialogView = getLayoutInflater().inflate(R.layout.remove_message_layout, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(RemoveActivity.this);
                alert.setTitle("Confirm")
                        .setView(dialogView)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fam.removeChild(position);
                                updateUI();
                            }
                        })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.show();

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