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
import com.example.applicazione_lista_regali.Utilities.CheckedContactsAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

public class ListCreationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CONTACT_PICK = 102;

    private static int count = 0;

    private TextInputEditText textName, textDescription, textBudget;
    private Button createButton, cancelButton;
    private ImageButton addContactsButton;
    private RecyclerView recyclerView;
    private CheckedContactsAdapter checkedContactsAdapter;
    private ArrayList<String> contactNameList;
    private ArrayList<Contatti> checkedContact = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_creation);

        textName = findViewById(R.id.text_name);
        textDescription = findViewById(R.id.text_description);
        textBudget = findViewById(R.id.text_budget);

        createButton = findViewById(R.id.btn_create);
        cancelButton = findViewById(R.id.btn_cancel);
        addContactsButton = findViewById(R.id.addContacts);

        createButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        addContactsButton.setOnClickListener(this);

        getSupportActionBar().setTitle(R.string.textCreate);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.addContacts: {
                Intent intent = new Intent(ListCreationActivity.this, SelectedContactsActivity.class);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);

                contactNameList = new ArrayList<>();
                for (Contatti cnt: checkedContact) {
                    contactNameList.add(cnt.getNome());
                }
                intent.putStringArrayListExtra("nomi", contactNameList);
                startActivityForResult(intent, CONTACT_PICK);
                break;
            }
            case R.id.btn_create: {
                Intent intent = new Intent();

                //CONTROLLO SUL NOME DELLA LISTA
                if(textName.getText().toString().isEmpty()) {
                    if(getIntent().getStringArrayListExtra("nomi").contains("Lista" + (getCount() + 1))) {
                        setCount(getCount() + 2);
                    } else
                        setCount(getCount() + 1);
                    intent.putExtra("nome", "Lista" + getCount());
                } else {
                    intent.putExtra("nome", Objects.requireNonNull(textName.getText()).toString());
                }

                //CONTROLLO SULLA DESCRIZIONE DELLA LISTA
                if(textDescription.getText().toString().isEmpty()) {
                    intent.putExtra("descrizione", "Nessuna Descrizione");
                } else {
                    intent.putExtra("descrizione", Objects.requireNonNull(textDescription.getText()).toString());
                }

                intent.putExtra("contatti", checkedContact);
                intent.putExtra("budget", textBudget.getText().toString());

                //CONTROLLA SE IL NOME DELLA LISTA ESISTE GIÀ
                if(!getIntent().getStringArrayListExtra("nomi").contains(textName.getText().toString())) {
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(ListCreationActivity.this, getString(R.string.inforequired), Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.btn_cancel: finish(); break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode == CONTACT_PICK) {
            if(resultCode == RESULT_OK) {
                ArrayList<Contatti> resultContact = intent.getParcelableArrayListExtra("contatti_scelti");
                checkedContact.addAll(resultContact);
                initRecyclerView();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.contactsList);
        recyclerView.setLayoutManager(linearLayoutManager);
        checkedContactsAdapter = new CheckedContactsAdapter(checkedContact);
        recyclerView.setAdapter(checkedContactsAdapter);
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        ListCreationActivity.count = count;
    }
}