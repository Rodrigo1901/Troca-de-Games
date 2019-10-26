package com.res.trocadejogos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.res.trocadejogos.Classes.Game;
import com.res.trocadejogos.Config.ConfigFirebase;
import com.res.trocadejogos.Views.Biblioteca;

public class EditarJogo extends AppCompatActivity {

    private StorageReference storageReference;
    private ImageView gameImage;
    private TextView gameName;
    private Switch troca;
    private Switch venda;
    private Button confirmar;
    private Game jogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_jogo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Adicionar jogo");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storageReference = ConfigFirebase.getFirebaseStorage();

        gameImage = findViewById(R.id.gameImage);
        gameName = findViewById(R.id.gameName);
        troca = findViewById(R.id.switchVenda);
        venda = findViewById(R.id.switchTroca);
        confirmar = findViewById(R.id.confirmAdd);


        storageReference.child("imagens").child("Games").child(Biblioteca.selectedGame + ".jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(EditarJogo.this).load(uri).into(gameImage);
            }
        });

        gameName.setText(Biblioteca.selectedGame);

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jogo = new Game();
                jogo.setNome(Biblioteca.selectedGame);
                jogo.setTroca("0");
                jogo.setVenda("0");

                if(troca.isChecked()){
                    jogo.setTroca("1");
                }
                if(venda.isChecked()){
                    jogo.setVenda("1");
                }
                jogo.salvar(Biblioteca.selectedGame);
                finish();
            }
        });
    }
}
