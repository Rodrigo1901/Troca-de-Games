package com.res.trocadejogos.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.res.trocadejogos.Classes.Game;
import com.res.trocadejogos.Config.ConfigFirebase;
import com.res.trocadejogos.R;

public class AdicionarJogo extends AppCompatActivity {

    private StorageReference storageReference;
    private ImageView gameImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_jogo);

        storageReference = ConfigFirebase.getFirebaseStorage();
        gameImage = findViewById(R.id.gameImage);

        storageReference.child("imagens").child("Games").child(Biblioteca.selectedGame + ".jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Toast.makeText(AdicionarJogo.this, Biblioteca.selectedGame, Toast.LENGTH_SHORT).show();
                Glide.with(AdicionarJogo.this).load(uri).into(gameImage);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Adicionar jogo");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }

}
