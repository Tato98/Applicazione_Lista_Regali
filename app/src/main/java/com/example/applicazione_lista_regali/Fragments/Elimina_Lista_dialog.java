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

public class Elimina_Lista_dialog extends DialogFragment {

    private Button buttonSi, buttonNo;
    private OnSendDataList onSendDataList;

    public Elimina_Lista_dialog(OnSendDataList onSendDataList) {
        this.onSendDataList = onSendDataList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.eliminate_dialog, container, false);

        buttonNo = view.findViewById(R.id.no);
        buttonSi = view.findViewById(R.id.si);

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        buttonSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = getArguments().getInt("posizione");
                onSendDataList.OnReceiveDataList(position);
                getDialog().dismiss();
            }
        });

        return view;
    }

    public interface OnSendDataList {
        void OnReceiveDataList(int position);
    }
}