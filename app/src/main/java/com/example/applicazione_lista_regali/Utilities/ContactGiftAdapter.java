package com.example.applicazione_lista_regali.Utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicazione_lista_regali.Models.Contatti;
import com.example.applicazione_lista_regali.R;

import java.util.ArrayList;

public class ContactGiftAdapter extends RecyclerView.Adapter<ContactGiftAdapter.ContactGiftHolder> {

    private ArrayList<Contatti> contatti;

    public class ContactGiftHolder extends RecyclerView.ViewHolder {

        TextView nome, numero, nomeRegalo, prezzo;
        ImageView immagineRegalo;

        public ContactGiftHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.contact_name);
            numero = itemView.findViewById(R.id.contact_number);
            nomeRegalo = itemView.findViewById(R.id.gift_name);
            prezzo = itemView.findViewById(R.id.gift_price);
            immagineRegalo = itemView.findViewById(R.id.gift_image);
        }
    }

    public ContactGiftAdapter(ArrayList<Contatti> contatti) {
        this.contatti = contatti;
    }

    @NonNull
    @Override
    public ContactGiftAdapter.ContactGiftHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_gift_view, parent, false);
        return new ContactGiftHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactGiftAdapter.ContactGiftHolder holder, int position) {
        holder.nome.setText(contatti.get(position).getNome());
        holder.numero.setText(contatti.get(position).getNumero());
        holder.nomeRegalo.setText(contatti.get(position).getNomeRegalo());
        holder.prezzo.setText(contatti.get(position).getPrezzoRegalo());
    }

    @Override
    public int getItemCount() {
        return contatti.size();
    }
}
