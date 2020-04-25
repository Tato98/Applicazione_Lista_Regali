package com.example.applicazione_lista_regali;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.applicazione_lista_regali.Utilities.ListAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

public class ListCreationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_CONTACT = 102;
    private TextInputEditText textName, textDescription;
    private Button createButton, cancelButton;
    private ImageButton addContactsButton;
    private TextView textView;
    private ArrayList<String> contactNameList = new ArrayList<>();
    private ArrayList<String> contactNumberList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_creation);

        textName = findViewById(R.id.text_name);
        textDescription = findViewById(R.id.text_description);

        textView = findViewById(R.id.text_view);

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
                intent.putExtra("nome", Objects.requireNonNull(textName.getText()).toString());
                intent.putExtra("descrizione", Objects.requireNonNull(textDescription.getText()).toString());
                intent.putStringArrayListExtra("lista_nomi", contactNameList);
                intent.putStringArrayListExtra("lista_numeri", contactNumberList);
                setResult(RESULT_OK, intent);
                finish();
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
                contactNameList = intent.getStringArrayListExtra("lista_nomi");
                contactNumberList = intent.getStringArrayListExtra("lista_numeri");

                textView.setText(contactNameList.get(0));
            }
        }
    }
}