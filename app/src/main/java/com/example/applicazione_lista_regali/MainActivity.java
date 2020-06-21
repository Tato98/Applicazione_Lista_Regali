package com.example.applicazione_lista_regali;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicazione_lista_regali.Fragments.ModifyDialog;
import com.example.applicazione_lista_regali.Models.Contatti;
import com.example.applicazione_lista_regali.Models.ListaRegali;
import com.example.applicazione_lista_regali.Utilities.ListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tooltip.Tooltip;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

//Activity principale dove è possibile visualizzare o far partire la creazione di una lista regali, modificare
// le liste già esistenti o eliminarle.
public class MainActivity extends AppCompatActivity implements ListAdapter.OnListListener, ModifyDialog.OnSendData {

    public static final int CREATE_REQUEST = 101;
    public static final int OPEN_REQUEST = 102;

    private Button addList;                 //bottone che permette di aggiu♂0gere una nuova lista regali
    private RecyclerView recyclerView;      //recycler view della lista di 'ListaRegali'
    private ListAdapter listAdapter;        //adapter che gestisce la lista
    private ArrayList<ListaRegali> lista;   //lista di 'ListaRegali'
    private ArrayList<String> nomiListe;    //lista dei nomi delle liste regali
    private Tooltip hintListCreation;       //tooltip del bottone per l'aggiunta di una lista

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addList = findViewById(R.id.addList);

        //Le seguenti righe di codice permettono di caricare direttamente dalle shared preferences
        // le liste regali che si sono salvate durante la fase di chiusura dell'app.
        //______________________________________________________________________________________________________________
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<ListaRegali>>() {}.getType();
        lista = gson.fromJson(json, type);
        //______________________________________________________________________________________________________________

        //Se al momento del caricamento la lista è vuota, viene correttamente inizializzata
        if(lista == null) {
            lista = new ArrayList<>();
        }

        //Visualizzazione del tooltip
        tooltipBuild();
        if(lista.isEmpty()) {
            hintListCreation.show();
        }

        //visualizzazione della lista
        initRecyclerView();

        //Gestione evento click del bottone che permette di creare una nuova lista
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListCreationActivity.class);
                nomiListe = new ArrayList<>();
                for (ListaRegali lr: lista) {
                    nomiListe.add(lr.getNome());
                }
                intent.putStringArrayListExtra("nomi", nomiListe);
                startActivityForResult(intent, CREATE_REQUEST);
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.gifty);
    }

    //Ovveride del metodo che esegue le ultime righe di codice prima che l'app si fermi
    @Override
    public void onStop() {
        super.onStop();

        //Le seguenti righe di codice permettono di salvare in formato json nelle shared preferences
        // la lista di 'ListaRegali' per poi riutilizzarle al momento di riapertura dell'app.
        //_________________________________________________________________________________________________________
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(lista);
        editor.putString("task list", json);
        editor.apply();
        //_________________________________________________________________________________________________________
    }

    //Ovveride del metodo che gestisce i risultati delle attività partite da questa activity.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode) {
            //Caso in cui arrivano i risultati per la creazione della lista
            case CREATE_REQUEST: {

                if(resultCode == RESULT_OK) {
                    ListaRegali listaRegali = intent.getParcelableExtra("lista_regali");
                    lista.add(listaRegali);

                    listAdapter.notifyDataSetChanged();

                    //Visualizzazione del tooltip
                    if(lista.isEmpty())
                        hintListCreation.show();
                    else
                        hintListCreation.dismiss();
                }
                break;
            }

            //Caso in cui arrivano i dati della lista regali aggiornati
            case OPEN_REQUEST: {

                if(resultCode == RESULT_OK) {
                    int posizione = intent.getIntExtra("posizione", 0);
                    ArrayList<Contatti> contattiAggiornati = intent.getParcelableArrayListExtra("contatti_aggiornati");
                    String budgetAggiornato = intent.getStringExtra("update_budget");
                    if(contattiAggiornati != null) {
                        lista.get(posizione).setContatti(contattiAggiornati);
                    }
                    if(budgetAggiornato != null) {
                        lista.get(posizione).setBudget(budgetAggiornato);
                        listAdapter.notifyDataSetChanged();
                    }
                }
                break;
            }
        }
    }

    //Inizializzazione della recycler view
    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(linearLayoutManager);
        listAdapter = new ListAdapter(lista, this);
        recyclerView.setAdapter(listAdapter);
    }

    //Override del metodo dell'interfaccia ListAdapter.OnListListener che gestisce il click di uno degli elementi della lista
    @Override
    public void OnListClick(int position) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("lista", lista);
        intent.putExtra("posizione", position);
        startActivityForResult(intent, OPEN_REQUEST);
    }

    //Override del metodo dell'interfaccia ListAdapter.OnListListener che gestisce il long click di uno degli elementi della lista
    @Override
    public void OnListLongClick(final int position, View view) {
        //Viene visualizzato un menu che permette di modificare o eliminare la lista selezionata
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.list_option_menu, popup.getMenu());
        popup.setGravity(Gravity.END);
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    //Caso in cui si vuole modificare la lista
                    case R.id.option1:
                        Bundle bundle = new Bundle();
                        nomiListe = new ArrayList<>();
                        for (ListaRegali lr: lista) {
                            nomiListe.add(lr.getNome());
                        }
                        bundle.putInt("posizione", position);
                        bundle.putStringArrayList("nomi_liste", nomiListe);
                        ModifyDialog modifyDialog = new ModifyDialog(MainActivity.this);
                        modifyDialog.setArguments(bundle);
                        modifyDialog.show(getSupportFragmentManager(),"ModifyDialog");
                        return true;

                    //Caso in cui si vuole eliminare la lista
                    case R.id.option2:

                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setTitle("Attenzione!");
                        alert.setMessage("Sei sicuro di voler eliminare questa lista?");
                        alert.setPositiveButton("si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                lista.remove(lista.get(position));
                                listAdapter.notifyItemRemoved(position);
                            }
                        });
                        alert.setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alert.create().show();
                        return true;
                }
                return false;
            }
        });
    }

    //Metodo che permette di costruire il tooltip
    public void tooltipBuild() {
        hintListCreation = new Tooltip.Builder(addList)
                .setText(R.string.hint_list_creation)
                .setCornerRadius(50f)
                .setGravity(Gravity.TOP)
                .setTextSize(15f)
                .setArrowHeight(100f)
                .setPadding(40f)
                .setArrowWidth(50f)
                .setTypeface(Typeface.DEFAULT)
                .setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryTrasparent))
                .build();
    }

    //Override del metodo dell'interfaccia ModifyDialog.OnSendData che permette di ricevere i nuovi valori
    // di nome e descrizione e impostarli alla lista regali.
    @Override
    public void OnReceiveData(String newName, String newDescription, int position) {
        if(!newName.isEmpty()) {
            lista.get(position).setNome(newName);
        }
        if(!newDescription.isEmpty()) {
            lista.get(position).setDescrizione(newDescription);
        }
        listAdapter.notifyItemChanged(position);
    }
}