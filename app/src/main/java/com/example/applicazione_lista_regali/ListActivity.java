package com.example.applicazione_lista_regali;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.applicazione_lista_regali.Fragments.Modifica_budget_frag;
import com.example.applicazione_lista_regali.Fragments.ShowListFragment;
import com.example.applicazione_lista_regali.Models.ListaRegali;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements ShowListFragment.OnSendTotSpent {

//___ I _______________________________________________________________________________________________

    private TextView b_rimasto;
    private double budget, spentBudget;
    private Button btnChange,canc_btn,modifica_btn;
    private EditText edit_budget;
    private Modifica_budget_frag modifica_budget_frag;
    boolean flag1, flag2;
//private String edited;
//____ F ______________________________________________________________________________________________


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        flag1 = true;
        flag2 = true;

        ArrayList<ListaRegali> lista = getIntent().getParcelableArrayListExtra("lista");
        int posizione = getIntent().getIntExtra("posizione", 0);

        ShowListFragment showListFragment = new ShowListFragment(lista, posizione, this);
        getSupportFragmentManager().beginTransaction().replace(R.id.show_list_fragment_container, showListFragment).commit();

        getSupportActionBar().setTitle(lista.get(posizione).getNome());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//__ I ________________________________________________________________________________________________

      //  textBudget = getIntent().getStringExtra( "budget");

        b_rimasto = findViewById(R.id.b_rimasto);       //text view
        btnChange = findViewById(R.id.btn);             //bottone slideup pannel
        canc_btn = findViewById(R.id.canc_btn);         //dialog btn
        modifica_btn = findViewById(R.id.modifica_btn); //dialog btn
        edit_budget = findViewById(R.id.edit_budget);

        budget = Double.parseDouble(lista.get(posizione).getBudget());
       // textBudget = getIntent().getStringExtra( "budget");

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

    @Override
    public void ReceiveTotSpent(double totSpent) {
        //ogni volta che si aggiunge un regalo o lo si modifica la variabile 'totSpent' conterrà il valore aggiornato dei soldi spesi
        setSpentBudget(totSpent);
        if((budget - getSpentBudget()) >= 0) {
            b_rimasto.setText(String.valueOf(budget - getSpentBudget()));
        } else {
            b_rimasto.setText("BUDGET SUPERATO");
        }
        showAlertDialog();
    }

    public void showAlertDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Attenzione!");
        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        if(flag1 && (getSpentBudget() >= 0.6 * budget && getSpentBudget() <=  0.8 * budget)) {
            alert.setMessage("Budget quasi a rischio.");
            alert.create().show();
            flag1 = false;
            flag2 = true;
        } else if(flag2 && (getSpentBudget() >= 0.8 * budget && getSpentBudget() <= budget)) {
            alert.setMessage("Budget a rischio.");
            alert.create().show();
            flag1 = true;
            flag2 = false;
        } else if(getSpentBudget() > budget) {
            alert.setMessage("Hai superato il budget. Puoi controllare il tuo budget " +
                    "nella sezione dedicata in basso trascinando il dito verso l'alto.");
            alert.create().show();
            flag1 = true;
            flag2 = true;
        }
    }

    public double getSpentBudget() {
        return spentBudget;
    }

    public void setSpentBudget(double spentBudget) {
        this.spentBudget = spentBudget;
    }
}