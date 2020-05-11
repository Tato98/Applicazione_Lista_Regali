package com.example.applicazione_lista_regali.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.applicazione_lista_regali.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;

public class ModifyGiftDialog extends DialogFragment {

    private static final String TAG = "ModifyGiftDialog";

    private TextInputEditText nome, prezzo;
    private Button cancellaButton, okButton;
    private String price;
    private DecimalFormat decimalFormat;
    private OnSendData onSendData;

    public ModifyGiftDialog(OnSendData onSendData) {
        this.onSendData = onSendData;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modify_gift_dialog, container, false);

        nome = view.findViewById(R.id.dialog_name);
        prezzo = view.findViewById(R.id.dialog_price);
        cancellaButton = view.findViewById(R.id.cancel);
        okButton = view.findViewById(R.id.ok);

        cancellaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final int position = getArguments().getInt("posizione");
                String currentPrice = getArguments().getString("current_price");

                double value;
                decimalFormat = new DecimalFormat("0.00");
                price = prezzo.getText().toString();
                if(!price.isEmpty()) {
                    value = Double.parseDouble(price);
                } else {
                    value = Double.parseDouble(currentPrice);
                }

                onSendData.OnReceiveData(nome.getText().toString(), decimalFormat.format(value), position);

                getDialog().dismiss();
            }
        });
        return view;
    }

    public interface OnSendData {
        void OnReceiveData(String newName, String newPrice, int position);
    }
}
