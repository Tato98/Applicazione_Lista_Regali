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

//La seguente classe permette di modificare il nome e il prezzo di uno specifico regalo
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

        //Anulla l'operazione chiudendo il Dialog
        cancellaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        //Raccoglie i valori inseriti nei campi richiesti per poi inviare i dati al "ShowListFragment"
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Si acquisisce dal bundle il valore della posizione e del prezzo corrente
                final int position = getArguments().getInt("posizione");
                String currentPrice = getArguments().getString("current_price");

                double value;
                decimalFormat = new DecimalFormat("0.00");
                price = prezzo.getText().toString();

                //Se viene inserito un nuovo prezzo allora salviamo tutto nella variabile value
                if(!price.isEmpty()) {
                    value = Double.parseDouble(price);
                }
                //Altrimenti value viene inizializzata con il prezzo vecchio
                else {
                    value = Double.parseDouble(currentPrice);
                }

                onSendData.OnReceiveData(nome.getText().toString(), decimalFormat.format(value), position);

                getDialog().dismiss();
            }
        });
        return view;
    }

    //Interfaccia che permette di inviare i dati appena modificati
    public interface OnSendData {
        void OnReceiveData(String newName, String newPrice, int position);
    }
}
