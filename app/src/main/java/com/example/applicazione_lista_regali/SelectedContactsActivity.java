package com.example.applicazione_lista_regali;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicazione_lista_regali.Models.Contatti;
import com.example.applicazione_lista_regali.Models.Regalo;
import com.example.applicazione_lista_regali.Utilities.ContactsAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class SelectedContactsActivity extends AppCompatActivity {

    private Contatti contatti;
    private ArrayList<Contatti> listaContatti;
    private RecyclerView recyclerView;
    private ContactsAdapter contactsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_contacts_activity);

        getAllContacts();
        initRecyclerView();

        getSupportActionBar().setTitle(R.string.phonebook);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void getAllContacts() {
        listaContatti = new ArrayList<>();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        if(cursor.getCount() > 0) {

            while(cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                if(Objects.requireNonNull(getIntent().getStringArrayListExtra("nomi")).contains(name))
                    contatti = new Contatti(name, number, new Regalo("vuoto", "0.00"), false);
                else
                    contatti = new Contatti(name, number, new Regalo("vuoto", "0.00"), true);

                listaContatti.add(contatti);
            }
            cursor.close();
        }
    }

    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.contacts);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        contactsAdapter = new ContactsAdapter(listaContatti);
        recyclerView.setItemViewCacheSize(listaContatti.size());
        recyclerView.setAdapter(contactsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_button, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.done: {
                Intent intent = new Intent();
                intent.putExtra("contatti_scelti", contactsAdapter.checkedContact);
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
            case android.R.id.home: {
                this.finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
