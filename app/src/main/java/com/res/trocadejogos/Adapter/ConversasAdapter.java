package com.res.trocadejogos.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.res.trocadejogos.Classes.Conversa;
import com.res.trocadejogos.Classes.Usuario;
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

    public ConversasAdapter(List<Conversa> lista, Context c) {
        this.conversas = lista;
        this.context = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_conversas, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Conversa conversa = conversas.get(position);
        holder.ultimaMensagem.setText(conversa.getUltimaMensagem());

        Usuario usuario = conversa.getUsuarioExibicao();
        holder.nome.setText(usuario.getNome());

        //FUDEU2
        /*
        if (usuario.getFoto() != null) {
            Uri uri = Uri.parse(usuario.getFoto());
            Glide.with(context).load(uri).into(holder.foto);
        } else {
            holder.foto.setImageResource(R.drawable.padrao);
        }
         */
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