package com.example.applicazione_lista_regali;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicazione_lista_regali.Models.Contatti;
import com.example.applicazione_lista_regali.Utilities.ContactsAdapter;

import java.util.ArrayList;

public class SelectedContactsActivity extends AppCompatActivity {

    private Contatti contatti;
    private ArrayList<Contatti> listaContatti;
    private ArrayList<String> checkedNameList, checkedNumberList;
    private Button doneButton;
    private RecyclerView recyclerView;
    private ContactsAdapter contactsAdapter;
    private CheckBox checkBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_contacts_activity);

        getAllContacts();
        initRecyclerView();

        checkBox = findViewById(R.id.check);

        doneButton = findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedNameList = new ArrayList<>();
                checkedNumberList = new ArrayList<>();
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
        });
    }

    public void getAllContacts() {
        listaContatti = new ArrayList<>();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        if(cursor.getCount() > 0) {

            while(cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contatti = new Contatti(name, number);
                listaContatti.add(contatti);
            }
            cursor.close();
        }
    }

    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.contacts);
        recyclerView.setLayoutManager(linearLayoutManager);
        contactsAdapter = new ContactsAdapter(listaContatti);
        recyclerView.setAdapter(contactsAdapter);
    }
}
