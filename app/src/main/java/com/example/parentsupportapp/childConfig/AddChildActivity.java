package com.example.parentsupportapp.childConfig;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parentsupportapp.ChildConfigActivity;
import com.example.parentsupportapp.R;
import com.example.parentsupportapp.model.Family;

public class AddChildActivity extends AppCompatActivity {
    private Family fam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        fam = Family.getInstance(this);
        setupBackButton();
        setupAddButton();
    }

    public void setupBackButton() {
        Button btn = findViewById(R.id.btnBack);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, AddChildActivity.class);
    }

    public void setupAddButton() {
        Button btn = findViewById(R.id.btnAdd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etFirstName = findViewById(R.id.etFirstName);
                String str = etFirstName.getText().toString();
                etFirstName.setHint("Enter Name");

                if (checkName(str, etFirstName) == -1){
                    return;
                }

                fam.addChild(str);
                etFirstName.setText("");
                Toast.makeText(AddChildActivity.this, "New Child Added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int checkName(String str, EditText et) {
        if (str.equals("")) {
            et.setHint("Enter again!");
            return -1;
        }
        return 0;
    }
}