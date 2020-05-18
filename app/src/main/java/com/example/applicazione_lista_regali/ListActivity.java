package com.example.applicazione_lista_regali;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.applicazione_lista_regali.Fragments.Modifica_budget_frag;
import com.example.applicazione_lista_regali.Fragments.ShowListFragment;
import com.example.applicazione_lista_regali.Models.ListaRegali;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements ShowListFragment.OnSendTotSpent, Modifica_budget_frag.OnSendBudget {

    private TextView b_rimasto;
    private double budget, spentBudget;
    private Button btnChange;
    boolean flag1, flag2;
    private TextView frase;
    private DecimalFormat decimalFormat;
    private ArrayList<ListaRegali> lista;
    private int posizione;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        flag1 = true;
        flag2 = true;
        decimalFormat = new DecimalFormat("0.00");

        lista = getIntent().getParcelableArrayListExtra("lista");
        posizione = getIntent().getIntExtra("posizione", 0);

        ShowListFragment showListFragment = new ShowListFragment(lista, posizione, this);
        getSupportFragmentManager().beginTransaction().replace(R.id.show_list_fragment_container, showListFragment).commit();

        b_rimasto = findViewById(R.id.b_rimasto);
        btnChange = findViewById(R.id.btn);
        frase = findViewById(R.id.frase);

        setBudget(Double.parseDouble(lista.get(posizione).getBudget()));

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Modifica_budget_frag modifica_budget_frag = new Modifica_budget_frag(ListActivity.this);
                modifica_budget_frag.show(getSupportFragmentManager(),"MODIFICA BUDGET");
            }
        });

        getSupportActionBar().setTitle(lista.get(posizione).getNome());
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

    @Override
    public void ReceiveTotSpent(double totSpent) {
        //ogni volta che si aggiunge un regalo o lo si modifica la variabile 'totSpent' conterrà il valore aggiornato dei soldi spesi
        setSpentBudget(totSpent);
        if(getBudget() - getSpentBudget() >= 0) {
            String tot = decimalFormat.format(getBudget() - getSpentBudget()) + " €";
            b_rimasto.setText(tot);
            b_rimasto.setTextColor(ContextCompat.getColor(ListActivity.this, R.color.bottom_nav_color));

        } else {
            String tot = decimalFormat.format(getSpentBudget() - getBudget()) + " €";
            b_rimasto.setText(tot);
            b_rimasto.setTextColor(ContextCompat.getColor(ListActivity.this, R.color.red));
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

        if(flag1 && (getSpentBudget() > 0.5 * getBudget() && getSpentBudget() <=  0.9 * getBudget())) {
            Toast.makeText(ListActivity.this, getString(R.string.budInfo1), Toast.LENGTH_LONG).show();
            flag1 = false;
            flag2 = true;
            frase.setText(R.string.budget_corrente);

        } else if(flag2 && (getSpentBudget() >= 0.9 * getBudget() && getSpentBudget() <= getBudget())) {
            Toast.makeText(ListActivity.this, getString(R.string.budInfo2), Toast.LENGTH_LONG).show();
            flag1 = true;
            flag2 = false;
            frase.setText(R.string.budget_corrente);
            b_rimasto.setTextColor(ContextCompat.getColor(ListActivity.this, R.color.edit_color));

        } else if(getSpentBudget() > getBudget()) {
            alert.setMessage(R.string.b_allert);
            alert.create().show();
            flag1 = true;
            flag2 = true;
            frase.setText(R.string.b_superato);

        } else {
            frase.setText(R.string.budget_corrente);
        }
    }

    public double getSpentBudget() {
        return spentBudget;
    }

    public void setSpentBudget(double spentBudget) {
        this.spentBudget = spentBudget;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    @Override
    public void OnReceiveBudget(String newBudget) {
        if(!newBudget.isEmpty()) {
            setBudget(Double.parseDouble(newBudget));
            ReceiveTotSpent(getSpentBudget());
            Update();
        }
    }

    private void Update() {
        Intent updateIntent = new Intent();
        updateIntent.putExtra("posizione", posizione);
        updateIntent.putExtra("update_budget", decimalFormat.format(getBudget()));
        setResult(RESULT_OK, updateIntent);
    }
}