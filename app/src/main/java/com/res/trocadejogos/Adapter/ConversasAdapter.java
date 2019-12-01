package com.res.trocadejogos.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.res.trocadejogos.Classes.Conversa;
import com.res.trocadejogos.Classes.Usuario;
import com.res.trocadejogos.Config.ConfigFirebase;
import com.res.trocadejogos.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Rodrigo Oliveira - rodrigoos19@gmail.com
 * @author Samuel Santana - samuel.santana1997@gmail.com
 */

public class ConversasAdapter extends RecyclerView.Adapter<ConversasAdapter.MyViewHolder> {

    private List<Conversa> conversas;
    private Context context;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    public ConversasAdapter(List<Conversa> lista, Context c) {
        this.conversas = lista;
        this.context = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_conversas, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Conversa conversa = conversas.get(position);
        holder.ultimaMensagem.setText(conversa.getUltimaMensagem());

        databaseReference = ConfigFirebase.getFirebaseDatabase();
        databaseReference.child("users").child(conversa.getIdDestinatario()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nome = dataSnapshot.child("nome").getValue(String.class);
                holder.nome.setText(nome);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        storageReference = ConfigFirebase.getFirebaseStorage();
        storageReference.child("imagens").child("perfil").child(conversa.getIdDestinatario() + ".jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.foto);
            }
        });


    }

    @Override
    public int getItemCount() {
        return conversas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView foto;
        TextView nome, ultimaMensagem;

        public MyViewHolder(View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imageViewFotoConversa);
            nome = itemView.findViewById(R.id.textNomeConversa);
            ultimaMensagem = itemView.findViewById(R.id.textUltimaMensagemConversa);
        }
    }
}