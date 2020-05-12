package com.example.applicazione_lista_regali.Utilities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicazione_lista_regali.Fragments.ModifyGiftDialog;
import com.example.applicazione_lista_regali.Models.Regalo;
import com.example.applicazione_lista_regali.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.GiftHolder> implements ModifyGiftDialog.OnSendData {

    private static final int DIALOG_FRAGMENT = 1;

    private ArrayList<Regalo> regali;
    private double totPrice;
    private DecimalFormat decimalFormat;
    private Fragment fragment;
    private OnUpdate onUpdate;

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
                    onUpdate.Update();
                    break;
                }
                case R.id.edit: {
                    Bundle bundle = new Bundle();
                    bundle.putInt("posizione", getAdapterPosition());
                    bundle.putString("current_price", regali.get(getAdapterPosition()).getPrezzo());
                    ModifyGiftDialog modifyGiftDialog = new ModifyGiftDialog(GiftAdapter.this);
                    modifyGiftDialog.setTargetFragment(fragment, DIALOG_FRAGMENT);
                    modifyGiftDialog.setArguments(bundle);
                    modifyGiftDialog.show(fragment.getFragmentManager(), "ModifyGiftDialog");
                    break;
                }
            }
        }
    }

    public GiftAdapter(ArrayList<Regalo> regali, Fragment fragment, OnUpdate onUpdate) {
        this.regali = regali;
        this.fragment = fragment;
        this.onUpdate = onUpdate;
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

    @Override
    public void OnReceiveData(String newName, String newPrice, int position) {
        if(!newName.isEmpty()) {
            regali.get(position).setNome(newName);
        }
        if(!newPrice.isEmpty()) {
            regali.get(position).setPrezzo(newPrice);
        }
        notifyItemChanged(position);
        onUpdate.Update();
    }

    public interface OnUpdate {
        void Update();
    }
}
