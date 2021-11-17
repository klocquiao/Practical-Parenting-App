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
        //
        ImageView img = findViewById(R.id.imgTester);
        loadImageFromStorage(fam.getChildren().get(0).getPortraitPath(),img);
        //
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ViewActivity.class);
    }

    public void loadImageFromStorage(String path, ImageView img)
    {

        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            //img=(ImageView)findViewById(R.id.imgPicker);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

}