package com.nicoitorma.qrattendancesystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class update_settings extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    public static final String CONTACT_NUM = "contactNum";
    public static final int PICK_IMAGE = 1;

    ImageView change_logo;
    TextView et_change_name, contactNum;
    Button save_changes;
    Uri imageUri;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_settings);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.color.mainColor));
        
        change_logo = findViewById(R.id.change_logo);
        change_logo.setDrawingCacheEnabled(true);
        et_change_name = findViewById(R.id.changename_et);
        contactNum = findViewById(R.id.contactNum_et);
        save_changes = findViewById(R.id.save_changes);

        Intent i = getIntent();
        String deptName = i.getStringExtra("deptName");
        String contact = i.getStringExtra("contact");
        et_change_name.setText(deptName);
        contactNum.setText(contact);

        change_logo.setOnClickListener(view1 -> {
            chooseImage();
        });

        save_changes.setOnClickListener(view1 -> {
            saveData();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void saveData() {
        preferences = MainActivity.instance.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(TEXT, et_change_name.getText().toString().trim());
        editor.putString(CONTACT_NUM, contactNum.getText().toString());
        editor.apply();
    }

    private void chooseImage() {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imageUri = data.getData();
        try {
            bitmap = MediaStore.Images.Media.getBitmap(MainActivity.instance.getContentResolver(), imageUri);
            change_logo.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}