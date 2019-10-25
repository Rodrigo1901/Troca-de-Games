package com.res.trocadejogos.Classes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.res.trocadejogos.Config.ConfigFirebase;
import com.res.trocadejogos.Config.FirebaseUser;

public class Game {

    private String venda, troca, nome;

    public Game() {
    }

    public void salvar(String nomeJogo){

        String userID = FirebaseUser.getIdentificadorUsuario();
        DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
        DatabaseReference game = firebaseRef.child("gameOwners").child(nomeJogo).child(userID);
        DatabaseReference gamelib = firebaseRef.child("library").child(userID).child(getNome());


        game.setValue(this);
        gamelib.setValue(this);

    }


    public Game(String proprietarios, String venda, String troca, String nome) {
        this.venda = venda;
        this.troca = troca;
        this.nome = nome;
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