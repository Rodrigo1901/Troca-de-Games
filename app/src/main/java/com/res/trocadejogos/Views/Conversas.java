package com.res.trocadejogos.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.res.trocadejogos.Adapter.ConversasAdapter;
import com.res.trocadejogos.Adapter.RecyclerItemClickListener;
import com.res.trocadejogos.Classes.Conversa;
import com.res.trocadejogos.Config.ConfigFirebase;
import com.res.trocadejogos.Config.FirebaseUser;
import com.res.trocadejogos.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Rodrigo Oliveira - rodrigoos19@gmail.com
 * @author Samuel Santana - samuel.santana1997@gmail.com
 */

public class Conversas extends AppCompatActivity {

    private List<Conversa> conversasList = new ArrayList<>();
    private List<Conversa> conversaPosition = new ArrayList<>();
    public static Conversa chat;
    private long backPressedTime;
    private Toast backToast;
    private String id;
    private String nome;
    private BottomNavigationView bottonNav;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private CircleImageView chatImage;
    private TextView nomeChat;
    private RecyclerView conversas;
    private ConversasAdapter adapter;
    private String identificadorUsuario;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversas);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Conversas");
        setSupportActionBar(toolbar);

        conversas = findViewById(R.id.recyclerListaConversas);
        databaseReference = ConfigFirebase.getFirebaseDatabase();
        identificadorUsuario = FirebaseUser.getIdentificadorUsuario();

        /*
        Intent it = getIntent();
        id = it.getStringExtra("id");
        nome = it.getStringExtra("nome");

        databaseReference = ConfigFirebase.getFirebaseDatabase();
        storageReference = ConfigFirebase.getFirebaseStorage();

        chatImage = findViewById(R.id.chatImage);
        nomeChat = findViewById(R.id.chatName);


        storageReference.child("imagens").child("perfil").child(id + ".jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(Conversas.this).load(uri).into(chatImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                chatImage.setImageResource(R.drawable.blank_profile_picture);
            }
        });

        nomeChat.setText(nome);

         */


        databaseReference.child("conversas").child(identificadorUsuario).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                conversasList.clear();
                for (DataSnapshot conversaSnapShot : dataSnapshot.getChildren()) {
                    conversasList.add(conversaSnapShot.getValue(Conversa.class));
                }

                adapter.notifyDataSetChanged();
                conversaPosition = conversasList;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        conversas.setLayoutManager(layoutManager);
        conversas.setHasFixedSize(true);
        adapter = new ConversasAdapter(conversasList, Conversas.this);
        conversas.setAdapter(adapter);
        conversas.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), conversas, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Conversa conversaSelecionada = conversaPosition.get(position);
                chat = conversaSelecionada;
                Intent it = new Intent(Conversas.this, Chat.class);
                startActivity(it);


            }

            @Override
            public void onLongItemClick(View view, final int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));



        bottonNav = findViewById(R.id.bottom_navigation);
        bottonNav.setSelectedItemId(R.id.menu_chat);
        bottonNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_library:
                        Intent it1 = new Intent(Conversas.this, Biblioteca.class);
                        startActivity(it1);
                        break;
                    case R.id.menu_map:
                        Intent it2 = new Intent(Conversas.this, Mapa.class);
                        startActivity(it2);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_conversas, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            moveTaskToBack(true);
            return;

        } else {
            backToast = Toast.makeText(Conversas.this, "Aperte novamente para sair", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}