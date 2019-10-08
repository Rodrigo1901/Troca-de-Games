package com.res.trocadejogos.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.res.trocadejogos.Classes.Usuario;
import com.res.trocadejogos.Config.Base64Custom;
import com.res.trocadejogos.Config.ConfigFirebase;
import com.res.trocadejogos.R;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cadastro");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fieldNome = (EditText) findViewById(R.id.fieldCadNome);
        fieldCEP = (EditText) findViewById(R.id.fieldCadCEP);
        fieldEmail = (EditText) findViewById(R.id.fieldCadEmail);
        fieldSenha = (EditText) findViewById(R.id.fieldCadSenha);
        fieldConfirmarSenha = (EditText) findViewById(R.id.fieldCadConfirmarSenha);
        cadButton = findViewById(R.id.cadButton);

        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNNNN-NNN");
        MaskTextWatcher mtw = new MaskTextWatcher(fieldCEP, smf);
        fieldCEP.addTextChangedListener(mtw);

        cadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validaCampos()) {
                    usuario = new Usuario();
                    usuario.setNome(fieldNome.getText().toString());
                    usuario.setCep(fieldCEP.getText().toString());
                    usuario.setEmail(fieldEmail.getText().toString());
                    usuario.setSenha(fieldSenha.getText().toString());
                    cadastrarUsuario(usuario);
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
            Toast.makeText(CadastroUsuario.this, "Nome inválido!", Toast.LENGTH_SHORT).show();
        } else if (isCampoVazio(cep)) {
            fieldCEP.requestFocus();
            Toast.makeText(CadastroUsuario.this, "CEP inválido!", Toast.LENGTH_SHORT).show();
        } else if (isCampoVazio(email)) {
            fieldEmail.requestFocus();
            Toast.makeText(CadastroUsuario.this, "Email inválido!", Toast.LENGTH_SHORT).show();
        } else if (isCampoVazio(senha)) {
            fieldSenha.requestFocus();
            Toast.makeText(CadastroUsuario.this, "Senha inválida!", Toast.LENGTH_SHORT).show();
        } else if (!isSenhasIguais(confirmarSenha, senha)) {
            Toast.makeText(CadastroUsuario.this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show();
            fieldConfirmarSenha.requestFocus();
        } else {
            res = true;
        }
        return res;
    }

    private boolean isCampoVazio(String valor) {

        boolean resultado = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
        return resultado;
    }

    private boolean isSenhasIguais(String confirmarSenha, String senha) {

        boolean resultado = (confirmarSenha.equals(senha));
        return resultado;
    }

    private void cadastrarUsuario(final Usuario usuario) {

        autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(CadastroUsuario.this, "Sucesso ao cadastrar usuário!", Toast.LENGTH_SHORT).show();
                    finish(); // Fecha a tela de cadastro e vai para a tela principal

                    try{

                        String idUser = Base64Custom.codificarBase64(usuario.getEmail());
                        usuario.setId(idUser);
                        usuario.salvar();

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        excecao = "Senha fraca!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Email inválido!";
                    } catch (FirebaseAuthUserCollisionException e) {
                        excecao = "Conta já cadastrada!";
                    } catch (Exception e) {
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroUsuario.this, excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_biblioteca, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_cancelar) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }*/
}