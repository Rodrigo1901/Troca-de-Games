package com.res.trocadejogos.Classes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.res.trocadejogos.Config.ConfigFirebase;
import com.res.trocadejogos.Config.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class Usuario {

    private String senha;
    private String id;
    private String nome;
    private String cep;
    private String email;

    public Usuario() {
    }

    public void salvar() {

        DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
        DatabaseReference usuario = firebaseRef.child("users").child(getId());

        usuario.setValue(this);
    }

    public void atualizarCep() {

        String userID = FirebaseUser.getIdentificadorUsuario();
        DatabaseReference database = ConfigFirebase.getFirebaseDatabase();

        DatabaseReference userRef = database.child("users").child(userID);

        Map<String, Object> userData = convertToMapCep();

        userRef.updateChildren(userData);
    }

    public void atualizarNome() {

        String userID = FirebaseUser.getIdentificadorUsuario();
        DatabaseReference database = ConfigFirebase.getFirebaseDatabase();

        DatabaseReference userRef = database.child("users").child(userID);

        Map<String, Object> userData = convertToMapNome();

        userRef.updateChildren(userData);
    }

    @Exclude
    public Map<String, Object> convertToMapNome() {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("nome", getNome());

        return userMap;
    }

    @Exclude
    public Map<String, Object> convertToMapCep() {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("cep", getCep());

        return userMap;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Exclude
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