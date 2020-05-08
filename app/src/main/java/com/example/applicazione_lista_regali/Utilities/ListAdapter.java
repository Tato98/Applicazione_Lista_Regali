package com.example.applicazione_lista_regali.Utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicazione_lista_regali.R;

import java.util.ArrayList;

import com.example.applicazione_lista_regali.Models.ListaRegali;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder> {

    private ArrayList<ListaRegali> lista;
    private OnListListener onListListener;

    public class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView Titolo, Descrizione, Budget;
        ImageView Immagine;
        Button bottone;
        OnListListener onListListener;

        public ListHolder(@NonNull View itemView, OnListListener onListListener) {
            super(itemView);
            Titolo = itemView.findViewById(R.id.text_title);
            Descrizione = itemView.findViewById(R.id.text_description);
            Immagine = itemView.findViewById(R.id.image_view);
            bottone = itemView.findViewById(R.id.choose_image_btn);
            Budget = itemView.findViewById(R.id.budget);

            this.onListListener = onListListener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onListListener.OnListClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            onListListener.OnListLongClick(getAdapterPosition(), v);
            return false;
        }
    }

    public ListAdapter(ArrayList<ListaRegali> lista, OnListListener onListListener) {
        this.lista = lista;
        this.onListListener = onListListener;
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view, parent, false);
        return new ListHolder(view, onListListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {
        try {
            holder.Titolo.setText(lista.get(position).getNome());
            holder.Descrizione.setText(lista.get(position).getDescrizione());
            String budget = lista.get(position).getBudget() + " €";
            holder.Budget.setText(budget);
            holder.Immagine.setImageResource(R.drawable.ic_image_black_24dp);
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
        void OnListLongClick(int position, View view);
    }
}
