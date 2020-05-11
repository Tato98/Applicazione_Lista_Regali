package com.example.applicazione_lista_regali.Utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicazione_lista_regali.Models.Contatti;
import com.example.applicazione_lista_regali.R;

import java.util.ArrayList;

public class CheckedContactsAdapter extends RecyclerView.Adapter<CheckedContactsAdapter.CheckedContactsHolder> {

    private ArrayList<Contatti> checkedContacts;

    public class CheckedContactsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nome, numero;
        ImageButton remove;

        public CheckedContactsHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.nome);
            numero = itemView.findViewById(R.id.numero);
            remove = itemView.findViewById(R.id.remove);

            remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.remove) {
                checkedContacts.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            }
        }
    }

    public CheckedContactsAdapter(ArrayList<Contatti> checkedContacts) {
        this.checkedContacts = checkedContacts;
    }

    @NonNull
    @Override
    public CheckedContactsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checked_contact_view, parent, false);
        return new CheckedContactsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckedContactsHolder holder, int position) {
        holder.nome.setText(checkedContacts.get(position).getNome());
        holder.numero.setText(checkedContacts.get(position).getNumero());
        holder.remove.setImageResource(R.drawable.ic_remove);
    }

    @Override
    public int getItemCount() {
        return checkedContacts.size();
    }
}
