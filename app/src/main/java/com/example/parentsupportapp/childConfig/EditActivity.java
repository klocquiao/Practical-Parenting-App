package com.example.parentsupportapp.childConfig;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.parentsupportapp.R;
import com.example.parentsupportapp.model.Child;
import com.example.parentsupportapp.model.Family;

public class EditActivity extends AppCompatActivity {

    private ListView listView;
    private Family fam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        fam = Family.getInstance(this);


        updateUI();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                View dialogView = getLayoutInflater().inflate(R.layout.edit_message_layout, null);

                EditText txt2 = (EditText)dialogView.findViewById(R.id.editTextNewName);
                AlertDialog.Builder alert = new AlertDialog.Builder(EditActivity.this);
                alert.setTitle("Edit Name")
                        .setView(dialogView)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String str = txt2.getText().toString();
                                Child child = fam.getChildren().get(position);
                                child.setFirstName(str);
                                updateUI();
                            }
                        });

                alert.show();
                updateUI();

            }
        });
    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, EditActivity.class);
    }

    private void updateUI() {
        fam = Family.getInstance(EditActivity.this);
        listView = findViewById(R.id.listChildrenToEdit);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(EditActivity.this, android.R.layout.simple_list_item_1,fam.getChildrenInString());
        listView.setAdapter(arrayAdapter);
    }
}