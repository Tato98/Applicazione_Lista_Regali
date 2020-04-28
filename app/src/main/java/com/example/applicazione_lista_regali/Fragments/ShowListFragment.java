package com.example.applicazione_lista_regali.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicazione_lista_regali.Models.Contatti;
import com.example.applicazione_lista_regali.R;
import com.example.applicazione_lista_regali.Utilities.CheckedContactsAdapter;

import java.util.ArrayList;

public class ShowListFragment extends Fragment {

    private RecyclerView recyclerView;
    private CheckedContactsAdapter checkedContactsAdapter;
    private ArrayList<Contatti> contacts;
    private ImageButton addPerson;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_list_fragment, container, false);

        ArrayList<String> listaNomi = getActivity().getIntent().getStringArrayListExtra("lista_nomi");
        ArrayList<String> listaNumeri = getActivity().getIntent().getStringArrayListExtra("lista_numeri");
        contacts = new ArrayList<>();

        createContactsList(contacts, listaNomi, listaNumeri);

        initRecyclerView(view);

        return view;
    }

    //crea una lista di contatti con i valori contenuti nelle due liste di stringhe
    public ArrayList<Contatti> createContactsList(ArrayList<Contatti> contacts, ArrayList<String> nameList, ArrayList<String> numberList) {
        for (String name: nameList) {
            for (String number: numberList) {
                if(nameList.indexOf(name.toString()) == numberList.indexOf(number.toString())) {
                    Contatti cnt = new Contatti(name, number);
                    contacts.add(cnt);
                }
            }
        }
        return contacts;
    }

    public void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.lista_regali);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        checkedContactsAdapter = new CheckedContactsAdapter(contacts);
        recyclerView.setAdapter(checkedContactsAdapter);
    }
}
