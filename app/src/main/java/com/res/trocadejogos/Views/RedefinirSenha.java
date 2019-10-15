package com.res.trocadejogos.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.res.trocadejogos.Config.ConfigFirebase;
import com.res.trocadejogos.R;

public class RedefinirSenha extends AppCompatActivity {

    private EditText email;
    private FirebaseAuth autenticacao;
    private Button botaoRedefinir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redefinir_senha);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Esqueceu a Senha");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email = findViewById(R.id.fieldRedefinirEmail);
        botaoRedefinir = findViewById(R.id.buttonRedefinirSenha);
        autenticacao = ConfigFirebase.getFirebaseAutenticacao();

        botaoRedefinir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isCampoVazio(email.getText().toString())) {
                    email.requestFocus();
                    Toast.makeText(RedefinirSenha.this, "Informe um email válido!", Toast.LENGTH_SHORT).show();
                } else {
                    autenticacao.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                email.setText("");

                                AlertDialog.Builder builder = new AlertDialog.Builder(RedefinirSenha.this);
                                builder.setTitle("Aviso");
                                builder.setMessage("Um email para redefinição de senha foi enviado para sua caixa de entrada!");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //finish();
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();

                            } else {
                                Toast.makeText(RedefinirSenha.this, "Usuário não cadastrado ou falha ao enviar email! Tente novamente.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean isCampoVazio(String valor) {

        boolean resultado = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
        return resultado;
    }
}