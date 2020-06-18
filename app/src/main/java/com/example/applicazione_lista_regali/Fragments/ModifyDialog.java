package com.example.applicazione_lista_regali.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.applicazione_lista_regali.R;

import java.util.ArrayList;
import java.util.Objects;

//Questa classe permette di gestire la modifica del nome e/o della descrizione delle liste regali
public class ModifyDialog extends DialogFragment {

    private static final String TAG = "ModifyDialog";

    private Button btnOK, btnCan;
    private EditText inputName,inputDes;
    private OnSendData onSendData;

    public ModifyDialog(OnSendData onSendData) {
        this.onSendData = onSendData;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modify_dialog, container, false);

         btnCan = view.findViewById(R.id.btn_cancella);
         btnOK = view.findViewById(R.id.btn_dialog);
         inputName = view.findViewById(R.id.dialog_name);
         inputDes = view.findViewById(R.id.dialog_description);

         //Anulla l'operazione in corso chiudendo il dialog
         btnCan.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 getDialog().dismiss();
             }
         });

         btnOK.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 final String name = Objects.requireNonNull(inputName.getText()).toString();
                 final String description = Objects.requireNonNull(inputDes.getText()).toString();

                 //Recupera dal Boundle inviato da "MainActivity" la lista dei nomi delle
                 // liste regali e la loro posizione nell'arrayList
                 ArrayList<String> listsName = getArguments().getStringArrayList("nomi_liste");
                 final int position = getArguments().getInt("posizione");

                 //Controlla che il nome inviato non sia già esistente
                 //Se non lo è inviamo i dati modificati alla "MainActivity"
                 if(!listsName.contains(name)) {
                     onSendData.OnReceiveData(name, description, position);
                     getDialog().dismiss();
                 }
                 //Se lo è genera un Toast
                 else {
                     Toast.makeText(getContext(), getString(R.string.inforequired), Toast.LENGTH_SHORT).show();
                 }
             }
         });

        return view;
    }

    //Interfaccia per inviare le modifiche fatte ai dati
    public interface OnSendData {
        void OnReceiveData(String newName, String newDescription, int position);
    }
}
