package com.res.trocadejogos.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.res.trocadejogos.R;

public class MainActivity extends AppCompatActivity {

    private TextView registrar;
    private Button entrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registrar = findViewById(R.id.textRegistrarButton);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent(MainActivity.this, CadastroUsuario.class);
                startActivity(it);
            }
        });

        entrar = findViewById(R.id.buttonEntrar);
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent(MainActivity.this, Biblioteca.class);
                startActivity(it);
            }
        });
    }
}