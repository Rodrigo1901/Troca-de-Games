package com.res.trocadejogos.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.res.trocadejogos.R;

public class Conversas extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversas);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Conversas");
        setSupportActionBar(toolbar);

        //TODO: Ajustar para vir selecionado o menu Conversas
        /*BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(myNavigationItemListener);
        bottomNavigationView.setSelectedItemId(R.id.my_menu_item_id);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_conversas, menu);

        return super.onCreateOptionsMenu(menu);
    }

}