package com.res.trocadejogos.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.res.trocadejogos.Config.ConfigFirebase;
import com.res.trocadejogos.R;

public class Biblioteca extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseAuth autenticacao;
    private long backPressedTime;
    private Toast backToast;
    private BottomNavigationView bottonNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblioteca);

        autenticacao = ConfigFirebase.getFirebaseAutenticacao();

        bottonNav = findViewById(R.id.bottom_navigation);
        bottonNav.setOnNavigationItemSelectedListener(navListener);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Biblioteca");
        setSupportActionBar(toolbar);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()){
                case R.id.menu_chat:

                    Intent it = new Intent(Biblioteca.this, Chat.class);
                    startActivity(it);

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