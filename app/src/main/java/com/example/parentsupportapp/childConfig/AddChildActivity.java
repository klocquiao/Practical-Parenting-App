package com.example.parentsupportapp.childConfig;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.parentsupportapp.R;
import com.example.parentsupportapp.model.Family;

public class AddChildActivity extends AppCompatActivity {
    private Family fam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        Toolbar toolbar = findViewById(R.id.tbAdd);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        fam = Family.getInstance(this);
        setupAddButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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