package com.example.qklahpita.draw;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_PERMISSION = 1;
    private FloatingActionButton fbCamera;
    private FloatingActionButton fbBrush;
    private FloatingActionMenu fbMenu;
    private GridView gvImages;
    private LinearLayout ln_image;
    private TextView txt_title_image;
    private TextView txt_add_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupPermission();
        setupUI();
        deleteImage();
    }

    private void setupPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION
                );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Warning!")
                        .setMessage("Without permission you can not use this app. " +
                                "Do you want to grant permission?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(
                                        MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_PERMISSION
                                );
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MainActivity.this.finish();
                            }
                        })
                        .show();
            }
        }
    }

    private void setupUI() {
        fbCamera = findViewById(R.id.fb_camera);
        fbBrush = findViewById(R.id.fb_brush);
        fbMenu = findViewById(R.id.fb_menu);
        gvImages = findViewById(R.id.gv_images);
        ln_image = findViewById(R.id.ln_image);
        txt_title_image = findViewById(R.id.txt_title_image);
        txt_add_image = findViewById(R.id.txt_add);
        fbCamera.setOnClickListener(this);
        fbBrush.setOnClickListener(this);
    }

    private void deleteImage(){
        gvImages.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Delete this image?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                File folder = new File(Environment.getExternalStorageDirectory().toString(), ImageUtils.folderName);
                                File[] listImage = folder.listFiles();
                                listImage[i].delete();
                                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                MediaScannerConnection.scanFile(
                                        getApplicationContext(),
                                        new String[]{listImage[i].getAbsolutePath()},
                                        null,
                                        null);
                                onStart();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, DrawActivity.class);
        if (view.getId() == R.id.fb_camera) {
            intent.putExtra("camera_mode", true);
        } else {
            intent.putExtra("camera_mode", false);
        }
        startActivity(intent);

        fbMenu.close(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GridImageAdapter gridImageAdapter = new GridImageAdapter(this);
        if (gridImageAdapter.getCount() > 0) {
            ln_image.setVisibility(View.GONE);
        } else {
            ln_image.setVisibility(View.VISIBLE);
            txt_title_image.setText("No Image");
            txt_add_image.setText("Tap + to create a new one");
        }
        gvImages.setAdapter(gridImageAdapter);
    }
}
