package com.res.trocadejogos.Config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigFirebase {

    private static FirebaseAuth autenticacao;
    private static DatabaseReference database;

    public static FirebaseAuth getFirebaseAutenticacao() {

        if (autenticacao == null) {
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

    public static DatabaseReference getFirebaseDatabase() {

        if (database == null) {
            database = FirebaseDatabase.getInstance().getReference();
        }
        return database;
    }
}