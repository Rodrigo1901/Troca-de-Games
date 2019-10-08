package com.res.trocadejogos.Classes;

import com.google.firebase.database.DatabaseReference;
import com.res.trocadejogos.Config.ConfigFirebase;

public class Usuario {

    private String senha;
    private String id;
    private String nome;
    private String cep;
    private String email;

    public Usuario() {
    }

    public void salvar(){

        DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
        DatabaseReference usuario = firebaseRef.child("users").child(getId());

        usuario.setValue(this);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}