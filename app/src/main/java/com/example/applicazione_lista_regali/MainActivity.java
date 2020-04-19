package com.example.applicazione_lista_regali;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ListaRegali.ListaRegali;

public class MainActivity extends AppCompatActivity {

    public static final int CREATE_REQUEST = 101;
    private Button addList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addList = findViewById(R.id.addList);
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListCreationActivity.class);
                startActivityForResult(intent, CREATE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == CREATE_REQUEST && resultCode == RESULT_OK) {
            String nome = intent.getExtras().getString("nome");
            String descrizione = intent.getExtras().getString("descrizione");

            ListaRegali lista = new ListaRegali(nome, descrizione);
        }
    }
}
