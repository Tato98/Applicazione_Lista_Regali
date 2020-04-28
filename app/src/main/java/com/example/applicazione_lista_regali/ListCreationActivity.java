package com.example.applicazione_lista_regali;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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

    private static final int PICK_CONTACT = 102;
    private static int count = 0;
    private TextInputEditText textName, textDescription;
    private Button createButton, cancelButton;
    private ImageButton addContactsButton;
    private RecyclerView recyclerView;
    private CheckedContactsAdapter checkedContactsAdapter;
    private ArrayList<String> resultName, resultNumber;
    private ArrayList<String> contactNameList = new ArrayList<>(), contactNumberList = new ArrayList<>();
    private ArrayList<Contatti> checkedContact = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_creation);

        textName = findViewById(R.id.text_name);
        textDescription = findViewById(R.id.text_description);

        createButton = findViewById(R.id.btn_create);
        cancelButton = findViewById(R.id.btn_cancel);
        addContactsButton = findViewById(R.id.addContacts);

        createButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        addContactsButton.setOnClickListener(this);

        getSupportActionBar().setTitle(R.string.textCreate);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.addContacts: {
                Intent intent = new Intent(ListCreationActivity.this, SelectedContactsActivity.class);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, PICK_CONTACT);
                break;
            }
            case R.id.btn_create: {
                Intent intent = new Intent();

                //CONTROLLO SUL NOME DELLA LISTA
                if(!getIntent().getStringArrayListExtra("nomi").contains(textName.getText().toString())) {
                    if(textName.getText().length() == 0) {
                        setCount(getCount() + 1);
                        intent.putExtra("nome", "Lista" + getCount());
                    } else if(textName.getText().toString().equals("Lista" + (getCount() + 1))) {
                        intent.putExtra("nome", Objects.requireNonNull(textName.getText()).toString());
                        setCount(getCount() + 1);
                    } else {
                        intent.putExtra("nome", Objects.requireNonNull(textName.getText()).toString());
                    }

                    //CONTROLLO SULLA DESCRIZIONE DELLA LISTA
                    if(textDescription.getText().length() == 0) {
                        intent.putExtra("descrizione", "Nessuna Descrizione");
                    } else {
                        intent.putExtra("descrizione", Objects.requireNonNull(textDescription.getText()).toString());
                    }
                    intent.putStringArrayListExtra("lista_nomi", contactNameList);
                    intent.putStringArrayListExtra("lista_numeri", contactNumberList);
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

        if(requestCode == PICK_CONTACT) {
            if(resultCode == RESULT_OK) {
                resultName = intent.getStringArrayListExtra("lista_nomi");
                resultNumber = intent.getStringArrayListExtra("lista_numeri");

                createContactsList(checkedContact, resultName, resultNumber);

                initRecyclerView();

                for (Contatti cnt: checkedContact) {
                    if(!contactNameList.contains(cnt.getNome()) && !contactNumberList.contains(cnt.getNumero())) {
                        contactNameList.add(cnt.getNome());
                        contactNumberList.add(cnt.getNumero());
                    }
                }
            }
        }
    }

    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.contactsList);
        recyclerView.setLayoutManager(linearLayoutManager);
        checkedContactsAdapter = new CheckedContactsAdapter(checkedContact);
        recyclerView.setAdapter(checkedContactsAdapter);
    }

    //crea una lista di contatti con i valori contenuti nelle due liste di stringhe
    public void createContactsList(ArrayList<Contatti> contacts, ArrayList<String> nameList, ArrayList<String> numberList) {
        for (String name: nameList) {
            for (String number: numberList) {
                if(nameList.indexOf(name.toString()) == numberList.indexOf(number.toString())) {
                    Contatti cnt = new Contatti(name, number);
                    contacts.add(cnt);
                }
            }
        }
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        ListCreationActivity.count = count;
    }
}