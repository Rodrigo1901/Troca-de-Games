package com.res.trocadejogos.Views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.res.trocadejogos.Adapter.MensagensAdapter;
import com.res.trocadejogos.Classes.Mensagem;
import com.res.trocadejogos.Classes.Usuario;
import com.res.trocadejogos.Config.ConfigFirebase;
import com.res.trocadejogos.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;
    private String id;
    private String nome;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private EditText editMensagem;
    private RecyclerView recyclerMensagens;
    private TextView textViewNome;
    private CircleImageView circleImageViewFoto;
    private Usuario usuarioDestinatario;
    private MensagensAdapter adapter;
    private List<Mensagem> mensagens = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent it = getIntent();
        id = it.getStringExtra("id");
        nome = it.getStringExtra("nome");

        //Configurando toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = ConfigFirebase.getFirebaseDatabase();
        storageReference = ConfigFirebase.getFirebaseStorage();


        //Configurações iniciais
        textViewNome = findViewById(R.id.textViewNomeChat);
        circleImageViewFoto = findViewById(R.id.circleImageFotoChat);
        editMensagem = findViewById(R.id.editMensagem);
        recyclerMensagens = findViewById(R.id.recyclerMensagens);

        textViewNome.setText(nome);

        storageReference.child("imagens").child("perfil").child(id + ".jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(Chat.this).load(uri).into(circleImageViewFoto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                circleImageViewFoto.setImageResource(R.drawable.blank_profile_picture);
            }
        });


        //TODO: Continuar após ter mapa pronto. Pois precisamos ter o usuario destinatario (Aula 308)
        /*Recuperar dados do usuario destinatario
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            usuarioDestinatario = (Usuario) bundle.getSerializable("chatContato");
            textViewNome.setText(usuarioDestinatario.getNome());
        }*/

        //Configuração adapter
        adapter = new MensagensAdapter(mensagens, getApplicationContext());

        //Configuração recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMensagens.setLayoutManager(layoutManager);
        recyclerMensagens.setHasFixedSize(true);
        recyclerMensagens.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            moveTaskToBack(true);
            return;

        } else {
            backToast = Toast.makeText(Chat.this, "Aperte novamente para sair", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    public void enviarMensagem(View view) {

        String textoMensagem = editMensagem.getText().toString();

        if (!textoMensagem.isEmpty()) {

            Mensagem mensagem = new Mensagem();
            //mensagem.setIdUsuario();
            mensagem.setMensagem(textoMensagem);

        } else {
            Toast.makeText(Chat.this, "Digite uma mensagem para enviar!", Toast.LENGTH_LONG).show();
        }
    }
}