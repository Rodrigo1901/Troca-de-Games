package com.res.trocadejogos.Config;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class FirebaseUser {

    public static String getIdentificadorUsuario(){

        FirebaseAuth usuario = ConfigFirebase.getFirebaseAutenticacao();
        String email = usuario.getCurrentUser().getEmail();
        String identificadorUsuario = Base64Custom.codificarBase64(email);

        return identificadorUsuario;

    }

    public static com.google.firebase.auth.FirebaseUser getCurrentUser(){
        FirebaseAuth usuario = ConfigFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();

    }

    public static boolean updateProfileImage(Uri url){

       try {

           com.google.firebase.auth.FirebaseUser user = getCurrentUser();
           UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setPhotoUri(url).build();
           user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   if(task.isSuccessful()){
                       Log.d("Perfil","Erro ao atualizar perfil.");
                   }
               }
           });
           return true;
       }catch (Exception e){
           e.printStackTrace();
           return false;
       }

    }




}
