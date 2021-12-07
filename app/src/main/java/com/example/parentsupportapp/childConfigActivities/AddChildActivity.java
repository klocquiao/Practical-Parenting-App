package com.example.parentsupportapp.childConfigActivities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * The AddChildActivity adds a new child to the family list. The associated data
 * per child that is added is a picture of them and their name.
 */

public class AddChildActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_GALLERY = 1;
    public static final int REQUEST_CODE_CAMERA = 2;
    public static final int MIN_SDK = 23;
    private Family family;
    public ImageView imgChild;
    private FloatingActionButton fabAddPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        Toolbar toolbar = findViewById(R.id.tbAdd);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        family = Family.getInstance(this);
        setupAddButton();
        setupImageButton();
        setupFabAddPhoto();
    }

    private void setupFabAddPhoto() {
        fabAddPhoto = findViewById(R.id.floatingActionButtonAddPhoto);
        fabAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });
    }

    private void setupImageButton() {
        imgChild = findViewById(R.id.imgAddChild);
        imgChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });
    }

    private void choosePhoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddChildActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.choose_dialog,null);
        builder.setCancelable(true);
        builder.setView(dialogView);

        TextView btnViewCamera = dialogView.findViewById(R.id.btnCamera);
        TextView btnViewGallery = dialogView.findViewById(R.id.btnGallery);

        AlertDialog alertDialogProfilePicture = builder.create();
        alertDialogProfilePicture.show();

        btnViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermission()) {
                    takePictureFromCamera();
                    alertDialogProfilePicture.cancel();
                }
            }
        });
        btnViewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureFromGallery();
                alertDialogProfilePicture.cancel();
            }
        });
    }

    private void takePictureFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_CODE_GALLERY);
    }

    private void takePictureFromCamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePicture, REQUEST_CODE_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    imgChild.setImageURI(selectedImageUri);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmapImage = (Bitmap) bundle.get(getString(R.string.child_config_data));
                    imgChild.setImageBitmap(bitmapImage);
                }
        }
    }

    private boolean checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= MIN_SDK) {
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
            Toast.makeText(AddChildActivity.this, getText(R.string.child_config_deny_permission), Toast.LENGTH_SHORT).show();
        }
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
                saveToInternalStorage(img, child.getPortraitPath(), AddChildActivity.this);

                family.addChild(child);
                finish();
            }
        });
    }

    private int checkName(String str, EditText et) {
        if (str.equals("")) {
            et.setHint(R.string.add_child_activity_enter_error);
            et.setHintTextColor(Color.RED);

            MediaPlayer song = MediaPlayer.create(AddChildActivity.this, R.raw.negativebeep);
            song.start();

            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));

            return -1;
        }
        return 0;
    }

    private String correctString(String str) {
        str = str.replaceAll("(?m)^[ \t]*\r?\n", "");
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
