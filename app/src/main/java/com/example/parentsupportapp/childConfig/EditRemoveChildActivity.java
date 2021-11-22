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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parentsupportapp.R;
import com.example.parentsupportapp.model.Child;
import com.example.parentsupportapp.model.Family;
import com.example.parentsupportapp.model.SaveImage;

import org.w3c.dom.Text;

public class EditRemoveChildActivity extends AppCompatActivity {

    private int position;
    private ImageView img;
    private EditText editText;
    private Family fam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_remove_child);

        Toolbar toolbar = findViewById(R.id.tbEditRemove);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        fam = Family.getInstance(this);

        Intent mIntent = getIntent();
        position = mIntent.getIntExtra("intVariableName", 0);

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
                alert.setTitle("Confirm")
                        .setView(dialogView)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fam.removeChild(position);
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
        img.setOnClickListener(new View.OnClickListener() {
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
                    img.setImageURI(selectedImageUri);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmapImage = (Bitmap) bundle.get("data");
                    img.setImageBitmap(bitmapImage);
                }
        }
    }

    private boolean checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
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
            Toast.makeText(EditRemoveChildActivity.this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupSaveButton() {
        img = findViewById(R.id.imgEditRemoveChild);
        editText = findViewById(R.id.editTextName);

        Button btnSave = findViewById(R.id.btnEditChild);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editText.getText().toString();
                if (checkName(str, editText) == -1){
                    return;
                }
                Child child = new Child(correctString(str));
                saveToInternalStorage(img, child.getPortraitPath(), EditRemoveChildActivity.this);

                fam.getChildren().remove(position);
                fam.getChildren().add(position, child);
                finish();
            }
        });
    }

    private int checkName(String str, EditText et) {
        if (str.equals("")) {
            et.setHint(R.string.add_child_activity_enter_error);
            et.setHintTextColor(Color.RED);
            return -1;
        }
        return 0;
    }

    private String correctString(String str) {
        str = str.replaceAll("(?m)^[ \t]*\r?\n", "");
        //str = str.replace(" ", "");
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
        Child child = fam.getChildren().get(position);
        img = findViewById(R.id.imgEditRemoveChild);
        loadImageFromStorage(child.getPortraitPath(), img, EditRemoveChildActivity.this);

        editText = findViewById(R.id.editTextName);
        editText.setText(child.getFirstName());
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