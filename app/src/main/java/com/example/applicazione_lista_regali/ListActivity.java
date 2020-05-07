package com.example.applicazione_lista_regali;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.applicazione_lista_regali.Fragments.ShowListFragment;
import com.example.applicazione_lista_regali.Models.ListaRegali;

public class ListActivity extends AppCompatActivity {

    private ShowListFragment showListFragment;
    private int posizione;
    private ListaRegali listaRegali;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        listaRegali = getIntent().getParcelableExtra("Lista");
        posizione = getIntent().getIntExtra("posizione", 0);

        showListFragment = new ShowListFragment(listaRegali.getContatti(), posizione);
        getSupportFragmentManager().beginTransaction().replace(R.id.show_list_fragment_container, showListFragment).commit();

        getSupportActionBar().setTitle(getIntent().getStringExtra("nome"));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}