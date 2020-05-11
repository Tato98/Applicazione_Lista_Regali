package com.example.applicazione_lista_regali;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.applicazione_lista_regali.Fragments.Fragment_modifica_dialog;
import com.example.applicazione_lista_regali.Fragments.Modifica_budget_frag;
import com.example.applicazione_lista_regali.Fragments.ShowListFragment;
import com.example.applicazione_lista_regali.Models.ListaRegali;


public class ListActivity extends AppCompatActivity {

//___ I _______________________________________________________________________________________________

    private TextView b_rimasto;
    private String textBudget;
    private Button btnChange,canc_btn,modifica_btn;
    private EditText edit_budget;
    private Modifica_budget_frag modifica_budget_frag;
//private String edited;
//____ F ______________________________________________________________________________________________


    private ShowListFragment showListFragment;
    private int posizione;
    private ListaRegali listaRegali;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        listaRegali = getIntent().getParcelableExtra("Lista");
        posizione = getIntent().getIntExtra("posizione", 0);

        showListFragment = new ShowListFragment(listaRegali.getContatti(), listaRegali.getBudget(), posizione);
        getSupportFragmentManager().beginTransaction().replace(R.id.show_list_fragment_container, showListFragment).commit();

        getSupportActionBar().setTitle(listaRegali.getNome());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//__ I ________________________________________________________________________________________________

      //  textBudget = getIntent().getStringExtra( "budget");

        b_rimasto = findViewById(R.id.b_rimasto);       //text view
        btnChange = findViewById(R.id.btn);             //bottone slideup pannel
        canc_btn = findViewById(R.id.canc_btn);         //dialog btn
        modifica_btn = findViewById(R.id.modifica_btn); //dialog btn
        edit_budget = findViewById(R.id.edit_budget);

        textBudget = listaRegali.getBudget();
       // textBudget = getIntent().getStringExtra( "budget");
        b_rimasto.setText(textBudget);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Modifica_budget_frag modifica_budget_frag = new Modifica_budget_frag();
                modifica_budget_frag.show(getSupportFragmentManager(),"MODIFICA BUDGET");
//modifica_budget_frag.getArguments().getBoolean(edited);
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
}