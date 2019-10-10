package com.res.trocadejogos.Config;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseUser {

    public static String getIdentificadorUsuario(){

        FirebaseAuth usuario = ConfigFirebase.getFirebaseAutenticacao();
        String email = usuario.getCurrentUser().getEmail();
        String identificadorUsuario = Base64Custom.codificarBase64(email);

        return identificadorUsuario;

    }




}
