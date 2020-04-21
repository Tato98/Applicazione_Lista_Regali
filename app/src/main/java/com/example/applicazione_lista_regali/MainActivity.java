package com.example.applicazione_lista_regali;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Objects;

import com.example.applicazione_lista_regali.ListaRegali.ListaRegali;
import com.example.applicazione_lista_regali.Utilities.ListAdapter;

public class MainActivity extends AppCompatActivity {

    public static final int CREATE_REQUEST = 101;
    private Button addList;
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private ListaRegali lista_regali;
    private ArrayList<ListaRegali> lista = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

            lista_regali = new ListaRegali(nome, descrizione);
            lista.add(lista_regali);

            recyclerView = findViewById(R.id.list);
            recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            listAdapter = new ListAdapter(lista);
            recyclerView.setAdapter(listAdapter);

            listAdapter.notifyDataSetChanged();
        }
    }
}
