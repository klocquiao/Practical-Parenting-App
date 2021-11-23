package com.example.parentsupportapp.childConfig;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Vibrator;

import com.example.parentsupportapp.R;
import com.example.parentsupportapp.model.Child;
import com.example.parentsupportapp.model.Family;
import com.example.parentsupportapp.model.SaveImage;

public class EditRemoveChildActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_GALLERY = 1;
    public static final int REQUEST_CODE_CAMERA = 2;
    public static final int MIN_SDK = 23;
    public static final String EXTRA_POSITION = "intVariableName";

    private int position;
    private ImageView imageViewEditChild;
    private EditText editTextNewChildName;
    private Family family;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_remove_child);

        Toolbar toolbar = findViewById(R.id.tbEditRemove);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        family = Family.getInstance(this);

        Intent mIntent = getIntent();
        position = mIntent.getIntExtra(EXTRA_POSITION, 0);

        setupChild();
        setupSaveButton();
        setupImageBtn();
        setupRemoveBtn();
    }

    private void setupRemoveBtn() {
        Button btn = findViewById(R.id.btnRemoveChild2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = getLayoutInflater().inflate(R.layout.remove_message_layout, null);
                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(EditRemoveChildActivity.this);
                alert.setTitle(getString(R.string.child_config_confirm))
                        .setView(dialogView)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                family.removeChild(position);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                alert.show();
            }
        });
    }

    private void setupImageBtn() {
        imageViewEditChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });
    }

    private void choosePhoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditRemoveChildActivity.this);
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
                    imageViewEditChild.setImageURI(selectedImageUri);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmapImage = (Bitmap) bundle.get(getString(R.string.child_config_data));
                    imageViewEditChild.setImageBitmap(bitmapImage);
                }
        }
    }

    private boolean checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= MIN_SDK) {
            int cameraPermission = ActivityCompat.checkSelfPermission(EditRemoveChildActivity.this, Manifest.permission.CAMERA);
            if (cameraPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(EditRemoveChildActivity.this, new String[]{Manifest.permission.CAMERA},20);
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
            Toast.makeText(EditRemoveChildActivity.this, getString(R.string.child_config_deny_permission), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupSaveButton() {
        imageViewEditChild = findViewById(R.id.imgEditRemoveChild);
        editTextNewChildName = findViewById(R.id.editTextName);

        Button btnSave = findViewById(R.id.btnEditChild);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editTextNewChildName.getText().toString();
                if (checkName(str, editTextNewChildName) == -1){
                    return;
                }
                Child child = new Child(correctString(str));
                saveToInternalStorage(imageViewEditChild, child.getPortraitPath(), EditRemoveChildActivity.this);

                family.getChildren().remove(position);
                family.getChildren().add(position, child);
                finish();
            }
        });
    }

    private int checkName(String str, EditText et) {
        if (str.equals("")) {
            et.setHint(R.string.add_child_activity_enter_error);
            et.setHintTextColor(Color.RED);

            MediaPlayer song = MediaPlayer.create(EditRemoveChildActivity.this, R.raw.negativebeep);
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

    private void setupChild() {
        Child child = family.getChildren().get(position);
        imageViewEditChild = findViewById(R.id.imgEditRemoveChild);
        loadImageFromStorage(child.getPortraitPath(), imageViewEditChild, EditRemoveChildActivity.this);

        editTextNewChildName = findViewById(R.id.editTextName);
        editTextNewChildName.setText(child.getFirstName());
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, EditRemoveChildActivity.class);
    }

    public static void loadImageFromStorage(String path, ImageView img, Context context) {
        Bitmap bitmap = new SaveImage(context).
                setFileName(path).
                load();
        img.setImageBitmap(bitmap);
    }
}