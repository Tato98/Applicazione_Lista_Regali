package com.example.applicazione_lista_regali.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicazione_lista_regali.Models.Contatti;
import com.example.applicazione_lista_regali.Models.Regalo;
import com.example.applicazione_lista_regali.R;

import java.util.ArrayList;

//Classe che rappresenta l'adapter della lista di contatti visualizzata nel ShowListFragment
public class ContactGiftAdapter extends RecyclerView.Adapter<ContactGiftAdapter.ContactGiftHolder> implements GiftAdapter.OnUpdate {

    private ArrayList<Contatti> contatti;
    private RecyclerView subRecyclerView;
    private GiftAdapter giftAdapter;
    private Context context;
    private Fragment fragment;
    private Notify notify;

    public class ContactGiftHolder extends RecyclerView.ViewHolder {

        TextView nome, numero, totRegali, totPrezzo;
        RelativeLayout contactContainer, expandableList;

        public ContactGiftHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.contact_name);
            numero = itemView.findViewById(R.id.contact_number);
            totRegali = itemView.findViewById(R.id.tot_gifts_value);
            totPrezzo = itemView.findViewById(R.id.tot_prices_value);

            contactContainer = itemView.findViewById(R.id.contacts_container);
            expandableList = itemView.findViewById(R.id.expadable_list);

            contactContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Contatti cnt = contatti.get(getAdapterPosition());
                    cnt.setExpanded(!cnt.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }

    public ContactGiftAdapter(ArrayList<Contatti> contatti, Context context, Fragment fragment, Notify notify) {
        this.contatti = contatti;
        this.context = context;
        this.fragment = fragment;
        this.notify = notify;
    }

    @NonNull
    @Override
    public ContactGiftHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_gift_view, parent, false);
        return new ContactGiftHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactGiftAdapter.ContactGiftHolder holder, int position) {
        holder.nome.setText(contatti.get(position).getNome());
        holder.numero.setText(contatti.get(position).getNumero());

        //Inizializza per ogni contatto un recycler view relativo alla lista regali
        subRecyclerView = holder.itemView.findViewById(R.id.gift_list);
        subRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        giftAdapter = new GiftAdapter(contatti.get(position).getRegali(), fragment, this);
        subRecyclerView.setAdapter(giftAdapter);

        holder.totRegali.setText(String.valueOf(giftAdapter.getItemCount()));
        String price = giftAdapter.getTotPrice() + " €";
        holder.totPrezzo.setText(price);

        boolean isExpanded = contatti.get(position).isExpanded();
        holder.expandableList.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    //Restituisce il numero di elementi della lista
    @Override
    public int getItemCount() {
        return contatti.size();
    }

    //Override del metodo dell'interfaccia OnUpdate di GiftAdapter.
    //Serve a lanciare il metodo dell'interfaccia di questa classe al fine di aggiornare le modifiche
    //apportate alla lista regali
    @Override
    public void Update() {
        notifyDataSetChanged();
        notify.notifyUpdate();
    }

    //Metodo che calcola il totale speso in base ai prezzi dei regali per ogni contatto
    public double totSpent() {
        double totSpent = 0;
        for (Contatti cnt: contatti) {
            for (Regalo r: cnt.getRegali()) {
                totSpent += Double.parseDouble(r.getPrezzo());
            }
        }
        return totSpent;
    }

    //Interfaccia che permette di notificare l'update della lista regali
    public interface Notify {
        void notifyUpdate();
    }
}
