package com.res.trocadejogos.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.res.trocadejogos.Classes.Usuario;
import com.res.trocadejogos.Config.ConfigFirebase;
import com.res.trocadejogos.R;

/**
 * @author Rodrigo Oliveira - rodrigoos19@gmail.com
 * @author Samuel Santana - samuel.santana1997@gmail.com
 */

public class MainActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private Button botaoEntrar;
    private TextView registrar, recuperarSenha;
    private FirebaseAuth autenticacao;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        campoEmail = findViewById(R.id.fieldLoginEmail);
        campoSenha = findViewById(R.id.fieldLoginSenha);
        botaoEntrar = findViewById(R.id.buttonEntrar);

        /* Chamada botão Entrar */
        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                if (!textoEmail.isEmpty()) {
                    if (!textoSenha.isEmpty()) {
                        usuario = new Usuario();
                        usuario.setEmail(textoEmail);
                        usuario.setSenha(textoSenha);
                        validarLogin();
                    } else {
                        campoSenha.requestFocus();
                        Toast.makeText(MainActivity.this, "Preencha a senha!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    campoEmail.requestFocus();
                    Toast.makeText(MainActivity.this, "Preencha o email!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* Chamada 'botão' Recuperar Senha */
        recuperarSenha = findViewById(R.id.textRecuperarSenha);
        recuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent(MainActivity.this, RedefinirSenha.class);
                startActivity(it);
            }
        });

        /* Chamada 'botão' Registrar */
        registrar = findViewById(R.id.textRegistrarButton);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent(MainActivity.this, CadastroUsuario.class);
                startActivity(it);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuarioLogado();
    }

    public void validarLogin() {

        autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    /*
                    Toast.makeText(MainActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_SHORT).show();
                    Abre tela principal do sistema
                     */
                    abrirTelaBiblioteca();

                    /* Verificar se usuário está logado */
                    verificarLogin();
                } else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        excecao = "Usuário não está cadastrado!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Email e senha não correspondem a um usuário cadastrado!";
                    } catch (Exception e) {
                        excecao = "Erro ao fazer login!" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(MainActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void abrirTelaBiblioteca() {
        startActivity(new Intent(this, Biblioteca.class));
    }

    public void verificarLogin() {

        autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() != null) {
            Toast.makeText(MainActivity.this, "Usuário logado!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Usuário deslogado!", Toast.LENGTH_SHORT).show();
        }
    }

    public void verificarUsuarioLogado() {

        autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() != null) {
            abrirTelaBiblioteca();
        }
    }
}