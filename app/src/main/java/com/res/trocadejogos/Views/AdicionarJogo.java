package com.res.trocadejogos.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
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
import com.res.trocadejogos.Config.FirebaseUser;
import com.res.trocadejogos.R;

public class AdicionarJogo extends AppCompatActivity {

    private StorageReference storageReference;
    private String identificadorUsuario;
    private ImageView gameImage;
    private TextView gameName;
    private Switch troca;
    private Switch venda;
    private Button confirmar;
    private Game jogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_jogo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Adicionar jogo");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storageReference = ConfigFirebase.getFirebaseStorage();
        identificadorUsuario = FirebaseUser.getIdentificadorUsuario();

        gameImage = findViewById(R.id.gameImage);
        gameName = findViewById(R.id.gameName);
        venda = findViewById(R.id.switchVenda);
        troca = findViewById(R.id.switchTroca);
        confirmar = findViewById(R.id.confirmAdd);

        Intent it = getIntent();
        final String selectedGame = it.getStringExtra("selectedGame");

        storageReference.child("imagens").child("Games").child(selectedGame + ".jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(AdicionarJogo.this).load(uri).into(gameImage);
            }
        });

        gameName.setText(selectedGame);

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jogo = new Game();
                jogo.setIdOwner(identificadorUsuario);
                jogo.setNome(selectedGame);
                jogo.setTroca("0");
                jogo.setVenda("0");

                if (troca.isChecked()) {
                    jogo.setTroca("1");
                }
                if (venda.isChecked()) {
                    jogo.setVenda("1");
                }
                jogo.salvar();
                finish();
            }
        });
    }
}