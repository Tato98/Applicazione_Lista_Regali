package com.example.applicazione_lista_regali;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicazione_lista_regali.Models.Contatti;
import com.example.applicazione_lista_regali.Models.ListaRegali;
import com.example.applicazione_lista_regali.Utilities.ListAdapter;
import com.tooltip.Tooltip;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements ListAdapter.OnListListener {

    public static final int CREATE_REQUEST = 101;
    private Button addList;
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private ListaRegali listaRegali;
    private ArrayList<ListaRegali> lista = new ArrayList<>();
    private ArrayList<String> nomiListe = new ArrayList<>();
    private ArrayList<String> contactName = new ArrayList<>();
    private ArrayList<String> contactNumber = new ArrayList<>();
    private ArrayList<Contatti> contact;
    private Tooltip hintListCreation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addList = findViewById(R.id.addList);
        tooltipBuild();
        hintListCreation.show();

        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListCreationActivity.class);
                for (ListaRegali lr: lista) {
                    nomiListe.add(lr.getNome());
                }
                intent.putStringArrayListExtra("nomi", nomiListe);
                startActivityForResult(intent, CREATE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == CREATE_REQUEST && resultCode == RESULT_OK) {

            String nome = Objects.requireNonNull(intent.getExtras()).getString("nome");
            String descrizione = intent.getExtras().getString("descrizione");
            contactName = intent.getStringArrayListExtra("lista_nomi");
            contactNumber = intent.getStringArrayListExtra("lista_numeri");

            listaRegali = new ListaRegali(nome, descrizione, createContactsList(new ArrayList<Contatti>(), contactName, contactNumber));
            lista.add(listaRegali);

            initRecyclerView();

            listAdapter.notifyDataSetChanged();

            if(lista.isEmpty())
                hintListCreation.show();
            else
                hintListCreation.dismiss();
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(linearLayoutManager);
        listAdapter = new ListAdapter(lista, this);
        recyclerView.setAdapter(listAdapter);
    }

    //crea una lista di contatti con i valori contenuti nelle due liste di stringhe
    public ArrayList<Contatti> createContactsList(ArrayList<Contatti> contacts, ArrayList<String> nameList, ArrayList<String> numberList) {
        for (String name: nameList) {
            for (String number: numberList) {
                if(nameList.indexOf(name) == numberList.indexOf(number)) {
                    Contatti cnt = new Contatti(name, number);
                    contacts.add(cnt);
                }
            }
        }
        return contacts;
    }

    @Override
    public void OnListClick(int position) {
        lista.get(position);
        contact = lista.get(position).getContatti();
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("nome", lista.get(position).getNome());
        intent.putExtra("descrizione", lista.get(position).getDescrizione());
        intent.putStringArrayListExtra("lista_nomi", lista.get(position).getContactsName(contact));
        intent.putStringArrayListExtra("lista_numeri", lista.get(position).getContactsNumber(contact));
        startActivity(intent);
    }

    public void tooltipBuild() {
        hintListCreation = new Tooltip.Builder(addList)
                .setText(R.string.hint_list_creation)
                .setCornerRadius(50f)
                .setGravity(Gravity.TOP)
                .setTextSize(15f)
                .setArrowHeight(100f)
                .setPadding(40f)
                .setArrowWidth(50f)
                .setTypeface(Typeface.DEFAULT)
                .setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryTrasparent))
                .build();
    }
}
