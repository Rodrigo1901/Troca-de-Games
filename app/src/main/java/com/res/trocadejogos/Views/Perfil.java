package com.res.trocadejogos.Views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.res.trocadejogos.Classes.Usuario;
import com.res.trocadejogos.Config.Base64Custom;
import com.res.trocadejogos.Config.ConfigFirebase;
import com.res.trocadejogos.Config.FirebaseUser;
import com.res.trocadejogos.Config.Permission;
import com.res.trocadejogos.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class Perfil extends AppCompatActivity {

    public String[] necessaryPermissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private ImageButton camButton, galleryButton;
    private static final int camSelect = 10;
    private static final int gallerySelect = 20;
    private CircleImageView circleImageViewPerfil;
    private StorageReference storageReference;
    private String identificadorUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        storageReference = ConfigFirebase.getFirebaseStorage();
        identificadorUsuario = FirebaseUser.getIdentificadorUsuario();

        Permission.validarPermissoes(necessaryPermissions, this, 1);

        camButton = findViewById(R.id.camButton);
        galleryButton = findViewById(R.id.galleryButton);
        circleImageViewPerfil = findViewById(R.id.profileImage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        com.google.firebase.auth.FirebaseUser usuario = FirebaseUser.getCurrentUser();
        Uri url = usuario.getPhotoUrl();

        if(url != null ){
            Glide.with(Perfil.this).load(url).into(circleImageViewPerfil);
        }else{
            circleImageViewPerfil.setImageResource(R.drawable.blank_profile_picture);
        }

        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (it.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(it, camSelect);
                }
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (it.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(it, gallerySelect);
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Bitmap image = null;

            try {

             switch (requestCode){

                 case camSelect:

                     image =(Bitmap)data.getExtras().get("data");
                     break;

                 case gallerySelect:
                     Uri imageLocation = data.getData();
                     image = MediaStore.Images.Media.getBitmap(getContentResolver(), imageLocation);
                     break;
                }

                if(image != null){

                    circleImageViewPerfil.setImageBitmap(image);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] imageData = baos.toByteArray();

                    final StorageReference imageRef = storageReference.child("imagens").child("perfil").child(identificadorUsuario + ".jpeg");

                    UploadTask uploadTask = imageRef.putBytes(imageData);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(Perfil.this, "Falha ao definir imagem", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(Perfil.this, "Sucesso ao definir imagem", Toast.LENGTH_SHORT).show();

                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri url) {

                                updateProfilePicture(url);


                                }
                            });

                        }
                    });

                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    public void updateProfilePicture(Uri url){
        FirebaseUser.updateProfileImage(url);
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