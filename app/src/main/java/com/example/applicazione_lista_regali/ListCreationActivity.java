package com.example.applicazione_lista_regali;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class ListCreationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText textName, textDescription;
    private Button createButton, cancelBotton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_creation);

        textName = findViewById(R.id.text_name);
        textDescription = findViewById(R.id.text_description);
        createButton = findViewById(R.id.btn_create);
        cancelBotton = findViewById(R.id.btn_cancel);

        createButton.setOnClickListener(this);
        cancelBotton.setOnClickListener(this);

        getSupportActionBar().setTitle(R.string.textCreate);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel: finish(); break;
            case R.id.btn_create: {
                Intent intent = new Intent();
                intent.putExtra("nome", Objects.requireNonNull(textName.getText()).toString());
                intent.putExtra("descrizione", Objects.requireNonNull(textDescription.getText()).toString());
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
        }
    }
}