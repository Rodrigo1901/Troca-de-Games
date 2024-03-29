package com.res.trocadejogos.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.res.trocadejogos.Config.Permission;
import com.res.trocadejogos.R;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

/**
 * @author Rodrigo Oliveira - rodrigoos19@gmail.com
 * @author Samuel Santana - samuel.santana1997@gmail.com
 */

public class Mapa extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;
    private SpinnerDialog spinner;
    private BottomNavigationView bottonNav;
    private FragmentManager fragmentManager;
    private String selectedGameMap;
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Mapa");
        setSupportActionBar(toolbar);

        Permission.validarPermissoes(permissoes, Mapa.this, 1);

        spinner = new SpinnerDialog(Mapa.this, (ArrayList<String>) Biblioteca.listaNome, "Selecione um jogo", "Fechar");

        bottonNav = findViewById(R.id.bottom_navigation);
        bottonNav.setSelectedItemId(R.id.menu_map);
        bottonNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_library:
                        Intent it1 = new Intent(Mapa.this, Biblioteca.class);
                        startActivity(it1);
                        break;
                    case R.id.menu_chat:
                        Intent it2 = new Intent(Mapa.this, Conversas.class);
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
        inflater.inflate(R.menu.menu_mapa, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            moveTaskToBack(true);
            return;
        } else {
            backToast = Toast.makeText(Mapa.this, "Aperte novamente para sair", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_pesquisar:

                spinner.showSpinerDialog();

                spinner.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String item, int position) {

                        selectedGameMap = item;
                        fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.add(R.id.mapaContainer, new MapaFragmento(Mapa.this, selectedGameMap), "MapaFragmento");
                        transaction.commitAllowingStateLoss();
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults) {

            //Permissão negada (denied) X Permissão concedida (granted)
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                //Alerta
                alertaValidarPermissao();
            } else if (permissaoResultado == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    private void alertaValidarPermissao() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Mapa.this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões!");
        builder.setCancelable(false);
        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Permission.validarPermissoes(permissoes, Mapa.this, 1);
            }
        });
        builder.setNegativeButton("NÃO PERMITIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent it = new Intent(Mapa.this, Biblioteca.class);
                startActivity(it);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}