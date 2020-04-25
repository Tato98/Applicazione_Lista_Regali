package com.example.applicazione_lista_regali;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicazione_lista_regali.Models.Contatti;
import com.example.applicazione_lista_regali.Models.ListaRegali;
import com.example.applicazione_lista_regali.Utilities.ListAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements ListAdapter.OnListListener {

    public static final int CREATE_REQUEST = 101;
    private Button addList;
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private ListaRegali listaRegali;
    private ArrayList<ListaRegali> lista = new ArrayList<>();
    private ArrayList<String> contactName = new ArrayList<>();
    private ArrayList<String> contactNumber = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //MainFragment fragment = new MainFragment();
        //getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

        addList = findViewById(R.id.addList);
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListCreationActivity.class);
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
            ArrayList<Contatti> contatti = new ArrayList<>();

            for (String name: contactName) {
                for (String number: contactNumber) {
                    Contatti cnt = new Contatti(name, number);
                    contatti.add(cnt);
                }
            }

            listaRegali = new ListaRegali(nome, descrizione, contatti);
            lista.add(listaRegali);

            initRecyclerView();

            listAdapter.notifyDataSetChanged();
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(linearLayoutManager);
        listAdapter = new ListAdapter(lista, this);
        recyclerView.setAdapter(listAdapter);
    }

    @Override
    public void OnListClick(int position) {
        lista.get(position);
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("nome", lista.get(position).getNome());
        intent.putExtra("descrizione", lista.get(position).getDescrizione());
        intent.putStringArrayListExtra("lista_nomi", contactName);
        intent.putStringArrayListExtra("lista_numeri", contactNumber);
        startActivity(intent);
    }
}
