package com.example.applicazione_lista_regali.Utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicazione_lista_regali.Models.Regalo;
import com.example.applicazione_lista_regali.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.GiftHolder> {

    private ArrayList<Regalo> regali;
    private double totPrice;
    private DecimalFormat decimalFormat;

    public class GiftHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nomeRegalo, prezzoRegalo;
        ImageButton delete, edit;


        public GiftHolder(@NonNull View itemView) {
            super(itemView);
            nomeRegalo = itemView.findViewById(R.id.gift_name);
            prezzoRegalo = itemView.findViewById(R.id.gift_price);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);

            delete.setOnClickListener(this);
            edit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.delete: {
                    regali.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    break;
                }
                case R.id.edit: {
                    break;
                }
            }
        }
    }

    public GiftAdapter(ArrayList<Regalo> regali) {
        this.regali = regali;
    }

    @NonNull
    @Override
    public GiftHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gift_view, parent, false);
        return new GiftHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftHolder holder, int position) {
        holder.nomeRegalo.setText(regali.get(position).getNome());
        String price = regali.get(position).getPrezzo() + " €";
        holder.prezzoRegalo.setText(price);
    }

    @Override
    public int getItemCount() {
        return regali.size();
    }

    public String getTotPrice() {
        decimalFormat = new DecimalFormat("0.00");
        for (Regalo r: regali) {
            totPrice += Double.parseDouble(r.getPrezzo());
        }
        return decimalFormat.format(totPrice);
    }
}
