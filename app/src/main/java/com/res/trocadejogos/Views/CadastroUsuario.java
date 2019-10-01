package com.res.trocadejogos.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.res.trocadejogos.Classes.Usuario;
import com.res.trocadejogos.Config.ConfigFirebase;
import com.res.trocadejogos.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CadastroUsuario extends AppCompatActivity {

    private EditText fieldNome;
    private EditText fieldCEP;
    private EditText fieldEmail;
    private EditText fieldSenha;
    private EditText fieldConfirmarSenha;
    private CardView cadButton;
    private FirebaseAuth autenticacao;
    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        fieldNome = (EditText)findViewById(R.id.fieldNome);
        fieldCEP =  (EditText)findViewById(R.id.fieldCEP);
        fieldEmail =(EditText)findViewById(R.id.fieldEmail);
        fieldSenha =(EditText)findViewById(R.id.fieldSenha);
        fieldConfirmarSenha = (EditText)findViewById(R.id.fieldConfirmarSenha);
        cadButton = findViewById(R.id.cadButton);

        cadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validaCampos()){
                    usuario = new Usuario();
                    usuario.setNome(fieldNome.getText().toString());
                    usuario.setCep(fieldCEP.getText().toString());
                    usuario.setEmail(fieldEmail.getText().toString());
                    usuario.setSenha(fieldSenha.getText().toString());
                    cadastrarUsuario();
                }
            }
        });

    }

    private boolean validaCampos() {

        boolean res = false;
        String nome = fieldNome.getText().toString();
        String cep = fieldCEP.getText().toString();
        String email = fieldEmail.getText().toString();
        String senha = fieldSenha.getText().toString();
        String confirmarSenha = fieldConfirmarSenha.getText().toString();

        if (isCampoVazio(nome)) {
            fieldNome.requestFocus();
            Toast.makeText(CadastroUsuario.this, "Nome invalido", Toast.LENGTH_SHORT).show();
        } else if (isCampoVazio(cep)) {
            fieldCEP.requestFocus();
            Toast.makeText(CadastroUsuario.this, "Cep invalido", Toast.LENGTH_SHORT).show();
        } else if (isCampoVazio(email)) {
            fieldEmail.requestFocus();
            Toast.makeText(CadastroUsuario.this, "Email invalido", Toast.LENGTH_SHORT).show();
        } else if (isCampoVazio(senha)) {
            fieldSenha.requestFocus();
            Toast.makeText(CadastroUsuario.this, "Senha invalida", Toast.LENGTH_SHORT).show();
        } else if (!isSenhasIguais(confirmarSenha, senha)) {
            Toast.makeText(CadastroUsuario.this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show();
            fieldConfirmarSenha.requestFocus();
        } else {
            res = true;
        }
        return res;
    }

    private boolean isCampoVazio(String valor){

        boolean resultado = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
        return resultado;

    }

    private boolean isSenhasIguais(String confirmarSenha, String senha){

        boolean resultado = (confirmarSenha.equals(senha));
        return resultado;
    }

    private void cadastrarUsuario(){

        autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Toast.makeText(CadastroUsuario.this, "Sucesso ao cadastrar usuário", Toast.LENGTH_SHORT).show();

                }else{
                    String excecao = "";
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Senha fraca!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Email inválido";
                    }catch (FirebaseAuthUserCollisionException e){
                        excecao = "Conta já cadastrada";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroUsuario.this, excecao, Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cad_usuario, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_cancelar){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
