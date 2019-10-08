package com.res.trocadejogos.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;

import com.res.trocadejogos.Config.Permission;
import com.res.trocadejogos.R;

public class Perfil extends AppCompatActivity {

    public String[] necessaryPermissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private ImageButton camButton, galleryButton;
    private static final int camSelect = 10;
    private static final int gallerySelect = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        Permission.validarPermissoes(necessaryPermissions, this, 1);

        camButton = findViewById(R.id.camButton);
        galleryButton = findViewById(R.id.galleryButton);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(it.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(it, camSelect);
                }


            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int permissionResult: grantResults){
            if(permissionResult == PackageManager.PERMISSION_DENIED){
                permissionAlert();
            }
        }

    }

    private void permissionAlert(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alerta!");
        builder.setMessage("Permissões necessárias");
        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}