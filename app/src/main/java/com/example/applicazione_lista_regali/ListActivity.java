package com.example.applicazione_lista_regali;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.applicazione_lista_regali.Fragments.ModifyDialog;
import com.example.applicazione_lista_regali.Fragments.Modifica_budget_frag;
import com.example.applicazione_lista_regali.Fragments.ShowListFragment;
import com.example.applicazione_lista_regali.Models.ListaRegali;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements ShowListFragment.OnSendTotSpent {

//___ I _______________________________________________________________________________________________

    private TextView b_rimasto;
    private double budget;
    private Button btnChange,canc_btn,modifica_btn;
    private EditText edit_budget;
    private Modifica_budget_frag modifica_budget_frag;

//____ F ______________________________________________________________________________________________


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        ArrayList<ListaRegali> lista = getIntent().getParcelableArrayListExtra("lista");
        int posizione = getIntent().getIntExtra("posizione", 0);

        ShowListFragment showListFragment = new ShowListFragment(lista, posizione, this);
        getSupportFragmentManager().beginTransaction().replace(R.id.show_list_fragment_container, showListFragment).commit();

        getSupportActionBar().setTitle(lista.get(posizione).getNome());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//__ I ________________________________________________________________________________________________



        b_rimasto = findViewById(R.id.b_rimasto);       //text view
        btnChange = findViewById(R.id.btn);             //bottone slideup pannel


        budget = Double.parseDouble(lista.get(posizione).getBudget());


        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Modifica_budget_frag modifica_budget_frag = new Modifica_budget_frag();
                modifica_budget_frag.show(getSupportFragmentManager(),"MODIFICA BUDGET");

            }
        });



//____ F ________________________________________________________________________________________________________
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void ReceiveTotSpent(double totSpent) {
        //ogni volta che si aggiunge un regalo o lo si modifica la variabile 'totSpent' conterrà il valore aggiornato dei soldi spesi
        //b_rimasto.setText(String.valueOf(budget - totSpent));
    }
}