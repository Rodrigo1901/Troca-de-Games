package com.res.trocadejogos.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.res.trocadejogos.Classes.Game;
import com.res.trocadejogos.Config.ConfigFirebase;
import com.res.trocadejogos.R;

import java.util.List;

/**
 * @author Rodrigo Oliveira - rodrigoos19@gmail.com
 * @author Samuel Santana - samuel.santana1997@gmail.com
 */

public class AdapterBiblioteca extends RecyclerView.Adapter<AdapterBiblioteca.MyViewHolder> {

    private List<Game> gameList;
    private Context context;
    private StorageReference storageReference;

    public AdapterBiblioteca(Context context, List<Game> games) {
        this.gameList = games;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_games, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Game game = gameList.get(position);
        holder.nome.setText(game.getNome());
        storageReference = ConfigFirebase.getFirebaseStorage();
        storageReference.child("imagens").child("Games").child(game.getNome() + ".jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.capa);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView capa;
        TextView nome;

        public MyViewHolder(View itemView) {
            super(itemView);

            capa = itemView.findViewById(R.id.gameImageLib);
            nome = itemView.findViewById(R.id.gameNameLib);
        }
    }
}