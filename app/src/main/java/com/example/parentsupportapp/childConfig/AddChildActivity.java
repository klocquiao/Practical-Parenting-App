package com.example.parentsupportapp.childConfig;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.parentsupportapp.R;
import com.example.parentsupportapp.model.Child;
import com.example.parentsupportapp.model.Family;
import com.example.parentsupportapp.model.SaveImage;

/**
 * Add child activity models adding a new new child to the
 * configuration
 */

public class AddChildActivity extends AppCompatActivity {
    private Family fam;
    public ImageView pick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        pick = (ImageView) findViewById(R.id.imgAddChild);
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        Toolbar toolbar = findViewById(R.id.tbAdd);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        fam = Family.getInstance(this);
        setupAddButton();
    }

    private void choosePhoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddChildActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.choose_camera_gallery_dialog_layout,null);
        builder.setCancelable(true);
        builder.setView(dialogView);

        ImageView imgViewCamera = dialogView.findViewById(R.id.imgCamera);
        ImageView imgViewGallery = dialogView.findViewById(R.id.imgGallery);

        AlertDialog alertDialogProfilePicture = builder.create();
        alertDialogProfilePicture.show();

        imgViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermission()) {
                    takePictureFromCamera();
                    alertDialogProfilePicture.cancel();
                }
            }
        });
        imgViewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureFromGallery();
                alertDialogProfilePicture.cancel();

            }
        });
    }

    private void takePictureFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    private void takePictureFromCamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePicture, 2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    pick.setImageURI(selectedImageUri);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmapImage = (Bitmap) bundle.get("data");
                    pick.setImageBitmap(bitmapImage);
                }
        }
    }

    private boolean checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int cameraPermission = ActivityCompat.checkSelfPermission(AddChildActivity.this, Manifest.permission.CAMERA);
            if (cameraPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(AddChildActivity.this, new String[]{Manifest.permission.CAMERA},20);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 20 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takePictureFromCamera();
        } else {
            Toast.makeText(AddChildActivity.this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
        }
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
        ImageView img = findViewById(R.id.imgAddChild);
        Button btn = findViewById(R.id.btnAdd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etFirstName = findViewById(R.id.etFirstName);
                String str = etFirstName.getText().toString();
                etFirstName.setHint(R.string.add_child_activity_enter_name);
                if (checkName(str, etFirstName) == -1){
                    return;
                }
                Child child = new Child(correctString(str));
                saveToInternalStorage(img,child.getPortraitPath(), AddChildActivity.this);

                fam.addChild(child);
                etFirstName.setText("");
                finish();
                Toast.makeText(AddChildActivity.this, getString(R.string.add_child_activity_added_toast), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int checkName(String str, EditText et) {
        if (str.equals("")) {
            et.setHint(R.string.add_child_activity_enter_error);
            return -1;
        }
        return 0;
    }

    private String correctString(String str) {
        str = str.replaceAll("(?m)^[ \t]*\r?\n", "");
        str = str.replace(" ", "");
        return str;
    }

    private void saveToInternalStorage(ImageView img, String path ,Context context){
        img.buildDrawingCache();
        Bitmap bitmapImage = img.getDrawingCache();
        new SaveImage(context).
                setFileName(path).
                save(bitmapImage);
    }
}