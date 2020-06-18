package com.example.applicazione_lista_regali;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicazione_lista_regali.Models.Contatti;
import com.example.applicazione_lista_regali.Models.Regalo;
import com.example.applicazione_lista_regali.Utilities.ContactsAdapter;

import java.util.ArrayList;
import java.util.Objects;

import static android.Manifest.permission.READ_CONTACTS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.content.pm.PackageManager.PERMISSION_DENIED;

//La seguente activity permette di visualizzare e selezionare i co♂0tatti presi dalla rubrica del dispositivo.
public class SelectedContactsActivity extends AppCompatActivity {

    private static final int ALL_PERMISSION_RESULT = 107;

    private Contatti contatti;                      //modello dei contatti che verrà inizializzato con i valori presi dalla rubrica contatti
    private ArrayList<Contatti> listaContatti;      //lista dei contatti che verrà visualizzata sulla pagina
    private RecyclerView recyclerView;              //recycler view della lista dei contatti
    private ContactsAdapter contactsAdapter;        //adapter per la gestione della lista contatti

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_contacts_activity);

        //Se i permessi alla rubrica sono negati viene visualizzata una schermata che richiede
        // di consentire all'app di fare utilizzo dei dati della rubrica
        if(checkSelfPermission(READ_CONTACTS) == PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(SelectedContactsActivity.this, new String[]{READ_CONTACTS}, ALL_PERMISSION_RESULT);
        }
        //Altrimenti viene richiamato il metodo getAllContacts()
        else {
            getAllContacts();
            initRecyclerView();
        }

        //_________________ Inizializzazione del titolo e del back button arrow ___________________
        getSupportActionBar().setTitle(R.string.phonebook);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //_________________________________________________________________________________________
    }

    //Metodo che permette di leggere i contatti della rubrica e salvarli in una lista di contatti
    public void getAllContacts() {
        listaContatti = new ArrayList<>();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        if(cursor.getCount() > 0) {

            //Fintanto che il cursore si muove verso il prossimo contatto...
            while(cursor.moveToNext()) {
                //... si salvano nome e numero del contatto in due stringhe...
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                //... se il contatto è stato già stato selezionato ed è presente nella lista dei contatti
                // inviata dall'activity precedente...
                if(Objects.requireNonNull(getIntent().getStringArrayListExtra("nomi")).contains(name))
                    //... viene creato un contatto la cui variabile isEnable è impostata a false.
                    // In questo modo il contatto è disabilitato e non potrà essere riselezionato.
                    contatti = new Contatti(name, number, new ArrayList<Regalo>(), false);
                //Altrimenti...
                else
                    //... viene creato un contatto la cui variabile isEnable è impostata a true.
                    // In questo modo il contatto è abilitato ed è possibile selezionarlo.
                    contatti = new Contatti(name, number, new ArrayList<Regalo>(), true);

                //Alla fine si aggiunge il contatto alla lista di contatti che verrà visualizzata su schermo.
                listaContatti.add(contatti);
            }
            cursor.close();
        }
    }

    //Metodo che inizializza il recycler view
    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.contacts);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        contactsAdapter = new ContactsAdapter(listaContatti);
        recyclerView.setItemViewCacheSize(listaContatti.size());
        recyclerView.setAdapter(contactsAdapter);
    }

    //In questo metodo viene creato e inizializzato il menu presente sulla action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_button, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            //Caso in cui si preme il bottone 'tick'
            case R.id.done: {
                //Si inizializzano i risultati e viene inviata dall'activity precedente
                // la lista dei contatti selezionati.
                Intent intent = new Intent();
                intent.putExtra("contatti_scelti", contactsAdapter.checkedContact);
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
            //Caso in cui si preme il back button arrow
            case android.R.id.home: {
                this.finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    //Override del metodo che gestisce la richiesta dei permessi
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == ALL_PERMISSION_RESULT) {
            //Se l'utente permette all'app di utilizzare la rubrica...
            if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                //... viene lanciato il metodo getAllContacts()...
                getAllContacts();
                initRecyclerView();
            }
            //... altrimenti...
            else {
                //... viene visualizzato un alert dialog che suggerisce all'utente di abilitare i permessi
                // per poter sfruttare a pieno le potenzialità dell'app, e ritorna all'activity precedente.
                AlertDialog.Builder alert = new AlertDialog.Builder(SelectedContactsActivity.this);
                alert.setTitle("Attenzione!");
                alert.setMessage("Per poter usufruire a pieno delle funzionalità di quest'app assicurati che i permessi richiesti siano garantiti.");
                alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alert.create().show();
            }
        }
    }
}
