package com.example.applicazione_lista_regali;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicazione_lista_regali.Fragments.InsertGiftDialog;
import com.example.applicazione_lista_regali.Models.Contatti;
import com.example.applicazione_lista_regali.Utilities.ContactGiftAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ListActivity extends AppCompatActivity {

    private static final int PICK_CONTACT = 102;
    public static final int DIALOG_FRAGMENT = 1;

    private RecyclerView recyclerView;
    private ContactGiftAdapter contactGiftAdapter;
    private int posizione;
    private ArrayList<String> listaNomi, listaNumeri;
    private ArrayList<Contatti> contatti = new ArrayList<>();
    private ArrayList<String> listaNomiAggiornata, listaNumeriAggiornata;
    private ArrayList<String> resultName, resultNumber;
    private ArrayList<String> contactNameList;
    private Intent updateIntent;
    private ImageButton addPerson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        listaNomi = getIntent().getStringArrayListExtra("lista_nomi");
        listaNumeri = getIntent().getStringArrayListExtra("lista_numeri");
        posizione = getIntent().getIntExtra("posizione", 0);

        contatti = createContactsList(contatti, listaNomi, listaNumeri);

        initRecyclerView();

        addPerson = findViewById(R.id.add_person);
        addPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, SelectedContactsActivity.class);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);

                contactNameList = new ArrayList<>();
                for (Contatti cnt: contatti) {
                    contactNameList.add(cnt.getNome());
                }
                intent.putStringArrayListExtra("nomi", contactNameList);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        getSupportActionBar().setTitle(getIntent().getStringExtra("nome"));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode) {
            case PICK_CONTACT: {
                if(resultCode == RESULT_OK) {
                    resultName = intent.getStringArrayListExtra("lista_nomi");
                    resultNumber = intent.getStringArrayListExtra("lista_numeri");

                    createContactsList(contatti, resultName, resultNumber);

                    contactGiftAdapter.notifyDataSetChanged();

                    Update();
                }
                break;
            }
            case DIALOG_FRAGMENT: {
                if(resultCode == RESULT_OK) {
                    String nomeRegalo = intent.getStringExtra("nome_regalo");
                    String prezzoRegalo = intent.getStringExtra("prezzo");
                    int pos = intent.getIntExtra("pos", 0);
                    contatti.get(pos).setNomeRegalo(nomeRegalo);
                    contatti.get(pos).setPrezzoRegalo(prezzoRegalo);

                    contactGiftAdapter.notifyDataSetChanged();
                }
                break;
            }
        }
    }

    Contatti deleteContact = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();

            switch(direction) {
                case ItemTouchHelper.LEFT: {
                    deleteContact = contatti.get(position);
                    contatti.remove(position);
                    contactGiftAdapter.notifyItemRemoved(position);
                    Update();
                    Snackbar.make(recyclerView, deleteContact.getNome(), Snackbar.LENGTH_LONG).setAction("Indietro", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            contatti.add(position, deleteContact);
                            contactGiftAdapter.notifyItemInserted(position);
                            Update();
                        }
                    }).show();

                    break;
                }
                case ItemTouchHelper.RIGHT: {
                    InsertGiftDialog insertGiftDialog = new InsertGiftDialog();
                    Intent intent = new Intent(ListActivity.this, InsertGiftDialog.class);
                    intent.putExtra("posizione", position);
                    startService(intent);
                    insertGiftDialog.show(getSupportFragmentManager(), "InsertGiftDialog");
                    contactGiftAdapter.notifyItemChanged(position);
                }
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            new RecyclerViewSwipeDecorator.Builder(ListActivity.this, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(ListActivity.this, R.color.remove_color))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_sweep)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(ListActivity.this, R.color.edit_color))
                    .addSwipeRightActionIcon(R.drawable.ic_edit)
                    .create()
                    .decorate();
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<Contatti> createContactsList(ArrayList<Contatti> contacts, ArrayList<String> nameList, ArrayList<String> numberList) {
        for (String name: nameList) {
            for (String number: numberList) {
                if(nameList.indexOf(name) == numberList.indexOf(number)) {
                    Contatti cnt = new Contatti(name, number);
                    contacts.add(cnt);
                }
            }
        }
        return contacts;
    }

    public void initRecyclerView() {
        recyclerView = findViewById(R.id.lista_regali);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactGiftAdapter = new ContactGiftAdapter(contatti);
        recyclerView.setAdapter(contactGiftAdapter);
        contactGiftAdapter.notifyDataSetChanged();
    }

    public void Update() {
        listaNomiAggiornata = new ArrayList<>();
        listaNumeriAggiornata = new ArrayList<>();
        for (Contatti cnt: contatti) {
            listaNomiAggiornata.add(cnt.getNome());
            listaNumeriAggiornata.add(cnt.getNumero());
        }
        updateIntent = new Intent();
        updateIntent.putStringArrayListExtra("nomi_aggiornati", listaNomiAggiornata);
        updateIntent.putStringArrayListExtra("numeri_aggiornati", listaNumeriAggiornata);
        updateIntent.putExtra("posizione", posizione);
        setResult(RESULT_OK, updateIntent);
    }
}