package com.example.applicazione_lista_regali.Utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicazione_lista_regali.R;

import java.util.ArrayList;

import com.example.applicazione_lista_regali.Models.ListaRegali;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CViewHolder> {

    private ArrayList<ListaRegali> lista;
    private OnListListener onListListener;

    public class CViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView Titolo, Descrizione;
        ImageView Immagine;
        OnListListener onListListener;

        public CViewHolder(@NonNull View itemView, OnListListener onListListener) {
            super(itemView);
            Titolo = itemView.findViewById(R.id.text_title);
            Descrizione = itemView.findViewById(R.id.text_description);
            Immagine = itemView.findViewById(R.id.image_view);
            this.onListListener = onListListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onListListener.OnListClick(getAdapterPosition());
        }
    }

    public ListAdapter(ArrayList<ListaRegali> lista, OnListListener onListListener) {
        this.lista = lista;
        this.onListListener = onListListener;
    }

    @NonNull
    @Override
    public CViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view, parent, false);
        return new CViewHolder(view, onListListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CViewHolder holder, int position) {
        try {
            holder.Titolo.setText(lista.get(position).getNome());
            holder.Descrizione.setText(lista.get(position).getDescrizione());
            holder.Immagine.setImageResource(R.drawable.ic_launcher_background);
        }
        catch(NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    //Serve a determinare quale lista si è cliccato
    public interface OnListListener {
        void OnListClick(int position);
    }
}
