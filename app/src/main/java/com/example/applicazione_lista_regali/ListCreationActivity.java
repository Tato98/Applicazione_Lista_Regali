package com.example.applicazione_lista_regali;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.applicazione_lista_regali.Models.Contatti;
import com.example.applicazione_lista_regali.Models.ListaRegali;
import com.example.applicazione_lista_regali.Utilities.CheckedContactsAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.util.ArrayList;

// Activity che gestisce la creazione di una nuova lista regali permettendo l'aggiunta del nome, della
// descrizione, del budget e della lista di contatti scelti dalla rubrica del disposotivo.
public class ListCreationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CONTACT_PICK = 102;

    private TextInputEditText textName, textDescription, textBudget;    //TextView del nome, della descrizione e del budget della lista
    private Button createButton, cancelButton;                          //bottoni per la creazione e l'anullamento della creazione della lista
    private ImageButton addContactsButton;                              //bottone per l'aggiunta dei contatti
    private RecyclerView recyclerView;                                  //recyclerView per la lista di contatti aggiunti
    private CheckedContactsAdapter checkedContactsAdapter;              //adapter per la gestione della lista dei contatti
    private ArrayList<String> contactNameList;                          //lista contenente i nomi dei contatti già scelti
    private ArrayList<Contatti> checkedContact = new ArrayList<>();     //lista dei contatti scelti dalla rubrica

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_creation);

        //________________________ Inizializzazione delle variabili _______________________________
        textName = findViewById(R.id.text_name);
        textDescription = findViewById(R.id.text_description);
        textBudget = findViewById(R.id.text_budget);

        createButton = findViewById(R.id.btn_create);
        cancelButton = findViewById(R.id.btn_cancel);
        addContactsButton = findViewById(R.id.addContacts);

        createButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        addContactsButton.setOnClickListener(this);
        //_________________________________________________________________________________________

        //__________________ Inizializzazione del titolo e del back button arrow __________________
        getSupportActionBar().setTitle(R.string.textCreate);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //_________________________________________________________________________________________
    }

    //Override del metodo che gestisce il click dei bottoni presenti nell'Activity
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //Caso in cui si preme il bottone per l'aggiunta dei regali
            case R.id.addContacts: {
                //Si crea un intent che porta all'activity che permette la scelta dei contatti
                Intent intent = new Intent(ListCreationActivity.this, SelectedContactsActivity.class);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);

                //Inizializzazione della lista dei nomi dei contatti che servirà  a precludere
                // la scelta di contatti già selezionati.
                contactNameList = new ArrayList<>();
                for (Contatti cnt: checkedContact) {
                    contactNameList.add(cnt.getNome());
                }
                intent.putStringArrayListExtra("nomi", contactNameList);
                startActivityForResult(intent, CONTACT_PICK);
                break;
            }
            //Caso in cui si preme il bottone per la creazione della lista
            case R.id.btn_create: {
                Intent intent = new Intent();
                ArrayList<String> nameList = getIntent().getStringArrayListExtra("nomi");

                //CONTROLLO SULLA DESCRIZIONE DELLA LISTA
                String description;
                if(textDescription.getText().toString().isEmpty()) {
                    description = "Nessuna Descrizione";
                } else {
                    description = textDescription.getText().toString();
                }

                //CONTROLLO SUL BUDGET DELLA LISTA
                double value;
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String budget = textBudget.getText().toString();
                if(!budget.isEmpty()) {
                    value = Double.parseDouble(budget);
                } else {
                    value = 0;
                }

                //CONTROLLO SUL NOME DELLA LISTA
                if(!textName.getText().toString().isEmpty()) {
                    String name = textName.getText().toString();

                    //CONTROLLA SE IL NOME DELLA LISTA ESISTE GIÀ
                    if(!nameList.contains(name)) {
                        ListaRegali listaRegali = new ListaRegali(name, description, checkedContact, decimalFormat.format(value));
                        intent.putExtra("lista_regali", listaRegali);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(ListCreationActivity.this, getString(R.string.inforequired), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ListCreationActivity.this, getString(R.string.inforequired_2), Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.btn_cancel: finish(); break;
        }
    }

    //Override del metodo che permette di gestire i risultati di determinati eventi partiti da questa activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        //Gestisce i contatti selezionati visualizzandoli nella pagina
        if(requestCode == CONTACT_PICK) {
            if(resultCode == RESULT_OK) {
                ArrayList<Contatti> resultContact = intent.getParcelableArrayListExtra("contatti_scelti");
                checkedContact.addAll(resultContact);
                initRecyclerView();
            }
        }
    }

    //Override del metodo che gestisce il click del back button arrow
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //Metodo che effettua l'inizializzazione della recycler view
    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.contactsList);
        recyclerView.setLayoutManager(linearLayoutManager);
        checkedContactsAdapter = new CheckedContactsAdapter(checkedContact);
        recyclerView.setAdapter(checkedContactsAdapter);
    }
}