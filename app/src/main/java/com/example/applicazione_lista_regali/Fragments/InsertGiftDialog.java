package com.example.applicazione_lista_regali.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.applicazione_lista_regali.Models.Regalo;
import com.example.applicazione_lista_regali.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;

import static android.app.Activity.RESULT_OK;

public class InsertGiftDialog extends DialogFragment {

    private static final String TAG = "InsertGiftDialog";

    private TextInputEditText nome, prezzo;
    private Button cancellaButton, okButton;
    private String name, price;
    private DecimalFormat decimalFormat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insert_gift_dialog, container, false);

        final int position = getArguments().getInt("posizione");

        nome = view.findViewById(R.id.gift_input_name);
        prezzo = view.findViewById(R.id.gift_input_price);
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

                double value;
                decimalFormat = new DecimalFormat("0.00");
                price = prezzo.getText().toString();
                if(!price.isEmpty()) {
                    value = Double.parseDouble(price);
                } else {
                    value = 0;
                }

                if(!nome.getText().toString().isEmpty() && !prezzo.getText().toString().isEmpty()) {
                    name = nome.getText().toString();

                    Regalo regalo = new Regalo(name, decimalFormat.format(value));
                    Intent intent = getActivity().getIntent();
                    intent.putExtra("regalo", regalo);
                    intent.putExtra("posizione", position);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
                    getDialog().dismiss();
                }
                else {
                    Toast.makeText(getContext(), getString(R.string.inforequired_3), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}
