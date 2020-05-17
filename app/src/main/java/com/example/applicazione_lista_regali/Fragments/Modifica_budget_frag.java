package com.example.applicazione_lista_regali.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.applicazione_lista_regali.R;

import java.text.DecimalFormat;
import java.util.Objects;

public class Modifica_budget_frag extends DialogFragment{


        private static final String TAG = " MODIFICA BUDGET ";

        private EditText edit_budget;
        private Button canc_btn, modifica_btn;
        private String budget;
        private DecimalFormat decimalFormat;
       // private OnSendBudget onSendBudget;

//public Modifica_budget_frag(OnSendBudget onSendBudget){this.onSendBudget=onSendBudget;}

    @Nullable
        @Override
        public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view =  inflater.inflate(R.layout.change_budget_dialog, null);

            canc_btn = view.findViewById(R.id.canc_btn);
            modifica_btn = view.findViewById(R.id.modifica_btn);
            edit_budget = view.findViewById(R.id.edit_budget);


            canc_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                }
            });

            modifica_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = getActivity().getIntent();
                    intent.putExtra("budget", Objects.requireNonNull(edit_budget.getText().toString()));

                    getDialog().dismiss();
                    Toast.makeText(getContext(), " BUDGET MODIFICATO ", Toast.LENGTH_LONG).show();
/*
                    final int position = getArguments().getInt("posizione");
                    String currentBudget = getArguments().getString("budget");

                    double value;
                    decimalFormat = new DecimalFormat("0.00");
                    budget = edit_budget.getText().toString();
                    if(!budget.isEmpty()) {
                        value = Double.parseDouble(budget);
                    } else {
                        value = Double.parseDouble(currentBudget);
                    }

                    onSendBudget.OnReceiveBudget( decimalFormat.format(value), position);


                    getDialog().dismiss();

 */

                }
            });
            return view;
        }
        /*
    public interface OnSendBudget {
        void OnReceiveBudget( String newBudget, int position);
    }

         */

    }


