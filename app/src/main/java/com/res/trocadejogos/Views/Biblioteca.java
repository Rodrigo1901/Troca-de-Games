package com.res.trocadejogos.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.res.trocadejogos.Config.ConfigFirebase;
import com.res.trocadejogos.R;

public class Biblioteca extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblioteca);

        autenticacao = ConfigFirebase.getFirebaseAutenticacao();

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Biblioteca");
        setSupportActionBar(toolbar);
    }

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

    public void onBackPressed(){
        finishAffinity();
    }
}