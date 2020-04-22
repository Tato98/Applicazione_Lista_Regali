package com.example.applicazione_lista_regali.Utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicazione_lista_regali.R;

import java.util.ArrayList;

import com.example.applicazione_lista_regali.ListaRegali.ListaRegali;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CViewHolder> {

    static class CViewHolder extends RecyclerView.ViewHolder {
        private TextView Titolo, Descrizione;
        private CViewHolder(@NonNull View itemView) {
            super(itemView);
            Titolo = itemView.findViewById(R.id.text_title);
            Descrizione = itemView.findViewById(R.id.text_description);
        }
    }

    private ArrayList<ListaRegali> lista;

    public ListAdapter(ArrayList<ListaRegali> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public CViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view, parent, false);
        return new CViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CViewHolder holder, int position) {
        try {
            holder.Titolo.setText(lista.get(position).getNome());
            holder.Descrizione.setText(lista.get(position).getDescrizione());
        }
        catch(NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}
