package com.res.trocadejogos.Classes;

import com.google.firebase.database.DatabaseReference;
import com.res.trocadejogos.Config.ConfigFirebase;
import com.res.trocadejogos.Config.FirebaseUser;

/**
 * @author Rodrigo Oliveira - rodrigoos19@gmail.com
 * @author Samuel Santana - samuel.santana1997@gmail.com
 */

public class Game {

    private String venda, troca, nome, idOwner;

    public Game() {
    }

    public void salvar() {

        String userID = FirebaseUser.getIdentificadorUsuario();
        DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
        DatabaseReference game = firebaseRef.child("gameOwners").child(getNome()).child(userID);
        DatabaseReference gamelib = firebaseRef.child("library").child(userID).child(getNome());

        game.setValue(this);
        gamelib.setValue(this);
    }

    public void remover() {

        String userID = FirebaseUser.getIdentificadorUsuario();
        DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
        DatabaseReference game = firebaseRef.child("gameOwners").child(getNome()).child(userID);
        DatabaseReference gamelib = firebaseRef.child("library").child(userID).child(getNome());

        game.removeValue();
        gamelib.removeValue();
    }


    public Game(String venda, String troca, String nome, String idOwner) {
        this.venda = venda;
        this.troca = troca;
        this.nome = nome;
        this.idOwner = idOwner;
    }

    public String getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(String idOwner) {
        this.idOwner = idOwner;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getVenda() {
        return venda;
    }

    public void setVenda(String venda) {
        this.venda = venda;
    }

    public String getTroca() {
        return troca;
    }

    public void setTroca(String troca) {
        this.troca = troca;
    }
}