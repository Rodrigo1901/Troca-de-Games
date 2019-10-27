package com.res.trocadejogos.Views;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.res.trocadejogos.Adapter.AdapterBiblioteca;
import com.res.trocadejogos.Classes.Game;
import com.res.trocadejogos.Config.ConfigFirebase;
import com.res.trocadejogos.Config.FirebaseUser;
import com.res.trocadejogos.R;

public class EditarJogo extends AppCompatActivity {

    private StorageReference storageReference;
    private String identificadorUsuario;
    private DatabaseReference dataRef;
    private ImageView gameImage;
    private TextView gameName;
    private Switch troca;
    private Switch venda;
    private Button confirmar;
    private Button remover;
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
        dataRef = ConfigFirebase.getFirebaseDatabase();
        identificadorUsuario = FirebaseUser.getIdentificadorUsuario();

        gameImage = findViewById(R.id.gameImage);
        gameName = findViewById(R.id.gameName);
        troca = findViewById(R.id.switchVenda);
        venda = findViewById(R.id.switchTroca);
        confirmar = findViewById(R.id.confirmAdd);
        remover = findViewById(R.id.deleteButton);

        dataRef.child("library").child(identificadorUsuario).child(Biblioteca.selectedGame).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Game games = dataSnapshot.getValue(Game.class);
                if(games.getTroca().equals("1")){
                    troca.setChecked(true);
                }
                if(games.getVenda().equals("1")){
                    venda.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
                jogo.salvar();
                finish();
            }
        });

        remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jogo = new Game();
                jogo.setNome(Biblioteca.selectedGame);
                jogo.remover();
                Intent it = new Intent(EditarJogo.this, Biblioteca.class);
                startActivity(it);
            }
        });
    }
}
