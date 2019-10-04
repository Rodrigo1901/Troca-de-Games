package com.res.trocadejogos.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.res.trocadejogos.R;

public class MainActivity extends AppCompatActivity {

    private TextView cad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

          cad = findViewById(R.id.textRegistrarButton);

        cad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent(MainActivity.this, CadastroUsuario.class);
                startActivity(it);

            }
        });
    }
}