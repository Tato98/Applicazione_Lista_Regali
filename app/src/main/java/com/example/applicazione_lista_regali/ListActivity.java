package com.example.applicazione_lista_regali;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.applicazione_lista_regali.Fragments.ModificaBudgetDialog;
import com.example.applicazione_lista_regali.Fragments.ShowListFragment;
import com.example.applicazione_lista_regali.Models.ListaRegali;

import java.text.DecimalFormat;
import java.util.ArrayList;

/*  Activity che gestisce la visualizzazione e la modifica del budget, della lista dei contatti
    e della lista dei regali.
    Frammento: ShowListFragment   */
public class ListActivity extends AppCompatActivity implements ShowListFragment.OnSendTotSpent, ModificaBudgetDialog.OnSendBudget {

    private TextView b_rimasto;                 //TextView che visualizza il budget rimanente
    private double budget, spentBudget;         //valori che indicano rispettivamente il budget e il totale speso
    private Button btnChange;                   //bottone che permette di modificare il budget
    boolean flag1, flag2;                       //variabili booleane utilizzate nel metodo 'showAlertDialog' (il loro utilizzo è spiegato nel metodo)
    private TextView frase;                     //TextView che visualizza la frase 'Hai ancora a disposizione:' o 'Hai superato il budget di:' al variare del totale speso
    private DecimalFormat decimalFormat;        //variabile utilizzata per effettuare un'approssimazione a due cifre decimali del budget
    private ArrayList<ListaRegali> lista;       //contiene la lista di 'ListaRegali'
    private int posizione;                      //contiene la posizione della 'ListaRegali' selezionata

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        //______________________________ Inizializzazione delle variabili utilizzate _______________________________________
        flag1 = true;
        flag2 = true;
        decimalFormat = new DecimalFormat("0.00");

        //La lista e la posizione sono passate direttamente dalla MainActivity
        lista = getIntent().getParcelableArrayListExtra("lista");
        posizione = getIntent().getIntExtra("posizione", 0);

        //Transazione verso il frammento
        ShowListFragment showListFragment = new ShowListFragment(lista, posizione, this);
        getSupportFragmentManager().beginTransaction().replace(R.id.show_list_fragment_container, showListFragment).commit();

        b_rimasto = findViewById(R.id.b_rimasto);
        btnChange = findViewById(R.id.btn);
        frase = findViewById(R.id.frase);

        setBudget(Double.parseDouble(lista.get(posizione).getBudget()));

        //Inizializzazione del bottone che permette di modificare il budget corrente
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fa partire il DialogFragment che gestisce la modifca del budget
                ModificaBudgetDialog modifica_budget_frag = new ModificaBudgetDialog(ListActivity.this);
                modifica_budget_frag.show(getSupportFragmentManager(),"MODIFICA BUDGET");
            }
        });

        //Le seguenti tre righe di codice servono a mostrare il titolo dell'activity
        // e la back arrow per tornare all'activity precedente
        getSupportActionBar().setTitle(lista.get(posizione).getNome());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.open_webpage_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Override del metodo che gestisce il click della back arrow
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            //Caso in cui si preme il bottone 'search'
            case R.id.search: {
                //Crea un intent che permette di collegarsi al sito web specificato dall'url indicato
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.amazon.it"));
                startActivity(browserIntent);
                break;
            }
            //Caso in cui si preme il back button arrow
            case android.R.id.home: {
                this.finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    //Metodo dell'interfaccia ShowListFragment.OnSendTotSpent che riceve il totale speso callcolato
    // sommando i prezzi dei regali per ogni contatto
    @Override
    public void ReceiveTotSpent(double totSpent) {
        //Ogni volta che si aggiunge un regalo o lo si modifica la variabile 'totSpent' conterrà
        // il valore aggiornato dei soldi spesi
        setSpentBudget(totSpent);

        //Se la differenza tra il budget e il totale speso è superiore o uguale a zero allora
        // si inizializza il budget rimasto a quella differenza
        if(getBudget() - getSpentBudget() >= 0) {
            String tot = decimalFormat.format(getBudget() - getSpentBudget()) + " €";
            b_rimasto.setText(tot);
            b_rimasto.setTextColor(ContextCompat.getColor(ListActivity.this, R.color.bottom_nav_color));

        }
        //Altrimenti lo si inizializza alla differenza inversa al fine di indicare la quantità con
        // la quale si è superato il budget
        else {
            String tot = decimalFormat.format(getSpentBudget() - getBudget()) + " €";
            b_rimasto.setText(tot);
            b_rimasto.setTextColor(ContextCompat.getColor(ListActivity.this, R.color.red));
        }
        showAlertDialog();
    }

    //Il seguente metodo serve a visualizzare un messaggio di alert dipendente dalla quantità
    // di budget speso
    public void showAlertDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Attenzione!");
        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        /* Se il totale speso supera la metà del budget ma è inferiore al 90% di esso allora viene
           visualizzato un Toast che avverte l'utente di tale situazione.
           La variabile flag1 (inizializzata a true) serve a evitare la ripetuta visualizzazione
           del sopracitato messaggio. Se infatti essa è a true allora l'if sottostante viene eseguito
           e la variabile flag1 viene posta a false. In questo modo ogni volta che l'utente
           inserisce/elimina un regalo oppure modifica il budget della lista o il prezzo di un
           qualsiasi regalo che fa rimanere il totale speso compreso tra la metà
           del budget e il suo 90%, il messaggio sarà visualizzato soltanto la prima volta. */

        if(flag1 && (getSpentBudget() > 0.5 * getBudget() && getSpentBudget() <=  0.9 * getBudget())) {
            Toast.makeText(ListActivity.this, getString(R.string.budInfo1), Toast.LENGTH_LONG).show();
            flag1 = false;
            flag2 = true;
            frase.setText(R.string.budget_corrente);

        }

        /* Se invece il totale speso supera il 90% del budget ma comunque è inferiore al suo valore
           allora viene visualizzato un messaggio che avverte l'utente che il budget sta per
           esaurirsi. Si hanno considerazioni analoghe alle precedenti per la variabile flag2. */

        else if(flag2 && (getSpentBudget() >= 0.9 * getBudget() && getSpentBudget() < getBudget())) {
            Toast.makeText(ListActivity.this, getString(R.string.budInfo2), Toast.LENGTH_LONG).show();
            flag1 = true;
            flag2 = false;
            frase.setText(R.string.budget_corrente);
            b_rimasto.setTextColor(ContextCompat.getColor(ListActivity.this, R.color.edit_color));

        }
        /* Se il totale è uguale al budget allora viene visualizzato un Alert che notifica tale situazione.
           Le variabili flag1 e flag2 sono riportate entrambe a true. */
        else if(getSpentBudget() == getBudget()){
            alert.setMessage(R.string.b_allert2);
            alert.create().show();
            flag1 = true;
            flag2 = true;
            frase.setText(R.string.b_esaurito);
        }

        /* Se invece il totale speso supera il budget allora viene visualizzato un Alert che
           notifica l'utente di tale situazione. Le variabili flag1 e flag2 sono riportate entrambe a true. */

        else if(getSpentBudget() > getBudget()) {
            alert.setMessage(R.string.b_allert);
            alert.create().show();
            flag1 = true;
            flag2 = true;
            frase.setText(R.string.b_superato);

        } else {
            frase.setText(R.string.budget_corrente);
        }
    }


    //________________________ Metodi Get e Set ____________________________
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
    //______________________________________________________________________


    //Metodo dell'interfaccia ModificaBudgetDialog.OnSendBudget che permette di ricevere il nuovo budget
    // dopo l'avvenuta modifica.
    @Override
    public void OnReceiveBudget(String newBudget) {
        if(!newBudget.isEmpty()) {
            setBudget(Double.parseDouble(newBudget));
            ReceiveTotSpent(getSpentBudget());
            Update();
        }
    }

    //Il seguente metodo permette di aggiornare la MainActivity di tutti i cambiamenti avvenuti
    // fino al quel momento alla lista regali.
    private void Update() {
        Intent updateIntent = new Intent();
        updateIntent.putExtra("posizione", posizione);
        updateIntent.putExtra("update_budget", decimalFormat.format(getBudget()));
        setResult(RESULT_OK, updateIntent);
    }
}