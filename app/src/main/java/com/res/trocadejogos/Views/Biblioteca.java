package com.res.trocadejogos.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.res.trocadejogos.Adapter.AdapterBiblioteca;
import com.res.trocadejogos.Adapter.RecyclerItemClickListener;
import com.res.trocadejogos.Classes.Game;
import com.res.trocadejogos.Config.ConfigFirebase;
import com.res.trocadejogos.Config.FirebaseUser;
import com.res.trocadejogos.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class Biblioteca extends AppCompatActivity {

    List<String> listaNome = new ArrayList<>();
    List<Game> games = new ArrayList<>();
    private FirebaseAuth autenticacao;
    private long backPressedTime;
    private Toast backToast;
    private SpinnerDialog spinner;
    private BottomNavigationView bottonNav;
    private String identificadorUsuario;
    private DatabaseReference dataRef;
    private FloatingActionButton botaoAdd;
    public static String selectedGame;
    private RecyclerView listaJogos;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblioteca);

        autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        dataRef = ConfigFirebase.getFirebaseDatabase();
        identificadorUsuario = FirebaseUser.getIdentificadorUsuario();
        storageReference = ConfigFirebase.getFirebaseStorage();

        listaJogos = findViewById(R.id.listaGames);
        botaoAdd = findViewById(R.id.addGameButton);
        bottonNav = findViewById(R.id.bottom_navigation);
        bottonNav.setOnNavigationItemSelectedListener(navListener);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Biblioteca");
        setSupportActionBar(toolbar);

        dataRef.child("library").child(identificadorUsuario).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<Game> gameList = new ArrayList<>();
                for(DataSnapshot gameSnapShot:dataSnapshot.getChildren()){
                    gameList.add(gameSnapShot.getValue(Game.class));//lista de jogos que estão na biblioteca do usuario logado
                }

                AdapterBiblioteca adapter = new AdapterBiblioteca(Biblioteca.this, gameList);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                listaJogos.setLayoutManager(layoutManager);
                listaJogos.setHasFixedSize(true);
                listaJogos.setAdapter(adapter);
                listaJogos.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), listaJogos, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Game gameClicked = gameList.get(position);
                        selectedGame = gameClicked.getNome();
                        Intent it = new Intent(Biblioteca.this, EditarJogo.class);
                        startActivity(it);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dataRef.child("gameList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Game> games = new ArrayList<>();
                for(DataSnapshot gameSnapShot:dataSnapshot.getChildren()){
                    games.add(gameSnapShot.getValue(Game.class));//lista de todos os jogos
                    //games = games - gameList
                }
                List(games);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spinner = new SpinnerDialog(Biblioteca.this, (ArrayList<String>) listaNome,"Selecione um jogo","Fechar");

        botaoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               spinner.showSpinerDialog();
            }
        });

        spinner.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                selectedGame = item;
                Intent it = new Intent(Biblioteca.this, AdicionarJogo.class);
                startActivity(it);
            }
        });
    }

    public void List(List<Game> gameList){

        games = gameList;
        for(Game game:gameList){
            listaNome.add(game.getNome());
        }
        Collections.sort(listaNome);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()){
                case R.id.menu_map:
                    Intent it1 = new Intent(Biblioteca.this, Mapa.class);
                    startActivity(it1);
                    break;
                case R.id.menu_chat:
                    Intent it2 = new Intent(Biblioteca.this, Conversas.class);
                    startActivity(it2);
                    break;
            }
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_biblioteca, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_deslogar:
                deslogarUsuario();
                verificarLogin(); // Verificar se usuário está logado
                finish();
                break;
            case R.id.action_perfil:
                editPerfil();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deslogarUsuario() {
        try {
            autenticacao.signOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editPerfil() {
        Intent it = new Intent(Biblioteca.this, Perfil.class);
        startActivity(it);
    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            moveTaskToBack(true);
            return;

        } else {
            backToast = Toast.makeText(Biblioteca.this, "Aperte novamente para sair", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    public void verificarLogin() {

        autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() != null) {
            Toast.makeText(this, "Usuário logado!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Usuário deslogado!", Toast.LENGTH_SHORT).show();
        }
    }
}