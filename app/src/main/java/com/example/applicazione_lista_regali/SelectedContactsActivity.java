package com.example.applicazione_lista_regali;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicazione_lista_regali.Models.Contatti;
import com.example.applicazione_lista_regali.Utilities.ContactsAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class SelectedContactsActivity extends AppCompatActivity {

    private Contatti contatti;
    private ArrayList<Contatti> listaContatti;
    private ArrayList<String> checkedNameList = new ArrayList<>();
    private ArrayList<String> checkedNumberList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactsAdapter contactsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_contacts_activity);

        getAllContacts();
        initRecyclerView();

        getSupportActionBar().setTitle(R.string.phonebook);
    }

    public void getAllContacts() {
        listaContatti = new ArrayList<>();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        if(cursor.getCount() > 0) {

            while(cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                if(Objects.requireNonNull(getIntent().getStringArrayListExtra("nomi")).contains(name))
                    contatti = new Contatti(name, number, false);
                else
                    contatti = new Contatti(name, number, true);

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

        if (item.getItemId() == R.id.done) {
            Intent intent = new Intent();

            for (Contatti contatti: contactsAdapter.checkedContact) {
                checkedNameList.add(contatti.getNome());
                checkedNumberList.add(contatti.getNumero());
            }

            intent.putStringArrayListExtra("lista_nomi", checkedNameList);
            intent.putStringArrayListExtra("lista_numeri", checkedNumberList);
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
