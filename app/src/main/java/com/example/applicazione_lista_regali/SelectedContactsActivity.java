package com.example.applicazione_lista_regali;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

public class SelectedContactsActivity extends AppCompatActivity {

    private static final int ALL_PERMISSION_RESULT = 107;

    private Contatti contatti;
    private ArrayList<Contatti> listaContatti;
    private RecyclerView recyclerView;
    private ContactsAdapter contactsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_contacts_activity);

        if(checkSelfPermission(READ_CONTACTS) == PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(SelectedContactsActivity.this, new String[]{READ_CONTACTS}, ALL_PERMISSION_RESULT);
        } else {
            getAllContacts();
            initRecyclerView();
        }

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
                    contatti = new Contatti(name, number, new ArrayList<Regalo>(), false);
                else
                    contatti = new Contatti(name, number, new ArrayList<Regalo>(), true);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == ALL_PERMISSION_RESULT) {
            if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                getAllContacts();
                initRecyclerView();
            } else {
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
