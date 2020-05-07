package com.example.applicazione_lista_regali.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import static android.app.Activity.RESULT_OK;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ListAdapter;

import com.example.applicazione_lista_regali.MainActivity;
import com.example.applicazione_lista_regali.R;

import java.util.Objects;

public class Fragment_modifica_dialog extends DialogFragment {

    private static final String TAG = "insertNameDialog";

    private Button btnOK, btnCan;
    private EditText inputName,inputDes;
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment, null);
         btnCan = view.findViewById(R.id.btn_cancella);
         btnOK = view.findViewById(R.id.btn_dialog);
         inputName = view.findViewById(R.id.dialog_title);
         inputDes = view.findViewById(R.id.dialog_des);

         btnOK.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 getDialog().dismiss();
             }
         });

         btnOK.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = getActivity().getIntent();
                 intent.putExtra("nome", Objects.requireNonNull(inputName.getText()).toString());
                 intent.putExtra("descrizione", Objects.requireNonNull(inputDes.getText()).toString());
                 getTargetFragment().onActivityResult(getTargetRequestCode(),RESULT_OK,intent);

                 getDialog().dismiss();
             }
         });
        return view;
    }
}
