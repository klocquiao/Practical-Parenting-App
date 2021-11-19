package com.example.parentsupportapp.childConfig;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import com.example.parentsupportapp.R;
import com.example.parentsupportapp.model.Family;
import com.example.parentsupportapp.model.SaveImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * View activity helps to view all the children that
 * have been added to the app
 */
public class ViewActivity extends AppCompatActivity {

    private ListView listView;
    private Family fam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Toolbar toolbar = findViewById(R.id.tbView);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        fam = Family.getInstance(this);
        listView = findViewById(R.id.listChildren);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ViewActivity.this, android.R.layout.simple_list_item_1,fam.getChildrenInString());
        listView.setAdapter(arrayAdapter);
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
}