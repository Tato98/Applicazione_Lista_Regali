package com.example.applicazione_lista_regali.Utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicazione_lista_regali.Models.Contatti;
import com.example.applicazione_lista_regali.R;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsHolder> {

    private ArrayList<Contatti> contatti;
    public ArrayList<Contatti> checkedContact = new ArrayList<>();
    private OnContactListener onContactListener;

    public class ContactsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nome, numero;
        CheckBox checkBox;
        OnContactListener onContactListener;

        public ContactsHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.name);
            numero = itemView.findViewById(R.id.number);
            checkBox = itemView.findViewById(R.id.check);

            checkBox.setOnClickListener(this);
        }

        public void setOnContactListener(OnContactListener onContactListener) {
            this.onContactListener = onContactListener;
        }

        @Override
        public void onClick(View v) {
            onContactListener.OnContactClick(v, getAdapterPosition());
        }
    }

    public ContactsAdapter(ArrayList<Contatti> contatti) {
        this.contatti = contatti;
    }

    @NonNull
    @Override
    public ContactsAdapter.ContactsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_view, parent, false);
        return new ContactsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactsAdapter.ContactsHolder holder, final int position) {
        try {
            holder.nome.setText(contatti.get(position).getNome());
            holder.numero.setText(contatti.get(position).getNumero());
            holder.setOnContactListener(new OnContactListener() {
                @Override
                public void OnContactClick(View view, int position) {
                    if(holder.checkBox.isChecked()) {
                        checkedContact.add(contatti.get(position));
                    }
                    else if(!holder.checkBox.isChecked()) {
                        checkedContact.remove(contatti.get(position));
                    }
                }
            });

            holder.nome.setEnabled(contatti.get(position).getIsEnable());
            holder.numero.setEnabled(contatti.get(position).getIsEnable());
            holder.checkBox.setEnabled(contatti.get(position).getIsEnable());
        }
        catch(NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return contatti.size();
    }

    public interface OnContactListener {
        void OnContactClick(View view, int position);
    }

    public ArrayList<Contatti> getContatti() {
        return contatti;
    }
}
