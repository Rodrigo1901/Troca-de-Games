package com.res.trocadejogos.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.res.trocadejogos.Classes.Usuario;
import com.res.trocadejogos.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CadastroUsuario extends AppCompatActivity {

    private EditText fieldNome;
    private EditText fieldCEP;
    private EditText fieldEmail;
    private EditText fieldTelefone;
    private EditText fieldSenha;
    private EditText fieldConfirmarSenha;
    private CardView cadButton;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private List<Usuario> listUsuario = new ArrayList<Usuario>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        inicializarFirebase();

        fieldNome = (EditText)findViewById(R.id.fieldNome);
        fieldCEP =  (EditText)findViewById(R.id.fieldCEP);
        fieldEmail =(EditText)findViewById(R.id.fieldEmail);
        fieldTelefone =(EditText)findViewById(R.id.fieldTelefone);
        fieldSenha =(EditText)findViewById(R.id.fieldSenha);
        fieldConfirmarSenha = (EditText)findViewById(R.id.fieldConfirmarSenha);

        cadButton = findViewById(R.id.cadButton);

        cadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validaCampos();

                databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Toast.makeText(getApplicationContext(), dataSnapshot.child("email").getValue().toString(), Toast.LENGTH_LONG).show();

                        boolean email = false;
                        for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                           Usuario user1 = userSnapshot.getValue(Usuario.class);
                           Toast.makeText(getApplicationContext(), user1.toString(), Toast.LENGTH_LONG).show();
                        }

                            if(fieldEmail.getText().equals(dataSnapshot.child("email"))){

                                email = true;
                                Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_LONG).show();

                            }


                        if(email){

                            Usuario u = new Usuario();
                            u.setNome(fieldNome.getText().toString());
                            u.setCep(fieldCEP.getText().toString());
                            u.setEmail(fieldEmail.getText().toString());
                            u.setTelefone(fieldTelefone.getText().toString());
                            u.setSenha(fieldSenha.getText().toString());
                            databaseReference.child("Usuario").child(u.getEmail()).setValue(u);
                            Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_LONG).show();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(CadastroUsuario.this, "Erro!", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });



    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(CadastroUsuario.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    private void validaCampos(){

        boolean res = false;

        String nome = fieldNome.getText().toString();
        String cep = fieldCEP.getText().toString();
        String email = fieldEmail.getText().toString();
        String phone = fieldTelefone.getText().toString();
        String senha = fieldSenha.getText().toString();
        String confirmarSenha = fieldConfirmarSenha.getText().toString();

        if(res = isCampoVazio(nome)){
            fieldNome.requestFocus();
        }
        else if(res = isCampoVazio(cep)){
            fieldCEP.requestFocus();
        }
        else if(res = !isEmailValido(email)){
            fieldEmail.requestFocus();
        }
        else if(res = isCampoVazio(phone)){
            fieldTelefone.requestFocus();
        }
        else if(res = isCampoVazio(senha)){
            fieldSenha.requestFocus();
        }
        else if(!isSenhasIguais(confirmarSenha, senha)){
            Toast.makeText(this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show();
            fieldConfirmarSenha.requestFocus();
        }

        if (res){

            Toast.makeText(this, "Há campos invalidos ou em branco", Toast.LENGTH_SHORT).show();

            //AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            //dlg.setTitle("Aviso");
            //dlg.setMessage("Há campos invalidos ou em branco");
            //dlg.show();
        }
    }


    private boolean isCampoVazio(String valor){

        boolean resultado = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
        return resultado;

    }

    private boolean isEmailValido(String email){

        boolean resultado = (!isCampoVazio(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        return resultado;

    }
    private boolean isSenhasIguais(String confirmarSenha, String senha){

        boolean resultado = (confirmarSenha.equals(senha));
        return resultado;
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
