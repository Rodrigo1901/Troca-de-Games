package com.res.trocadejogos.Views;

import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        //Configurando toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configurações iniciais
        textViewNome = findViewById(R.id.textViewNomeChat);
        circleImageViewFoto = findViewById(R.id.circleImageFotoChat);
        editMensagem = findViewById(R.id.editMensagem);
        recyclerMensagens = findViewById(R.id.recyclerMensagens);

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