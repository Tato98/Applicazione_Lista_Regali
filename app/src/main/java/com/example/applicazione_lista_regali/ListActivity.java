package com.example.applicazione_lista_regali;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.applicazione_lista_regali.Fragments.BudgetFragment;
import com.example.applicazione_lista_regali.Fragments.SearchFragment;
import com.example.applicazione_lista_regali.Fragments.ShowListFragment;
import com.example.applicazione_lista_regali.Models.Contatti;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private ShowListFragment showListFragment;
    private BudgetFragment budgetFragment;
    private SearchFragment searchFragment;
    private ArrayList<String> listaNomi, listaNumeri;
    private ArrayList<Contatti> contatti = new ArrayList<>();
    private int posizione;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        listaNomi = getIntent().getStringArrayListExtra("lista_nomi");
        listaNumeri = getIntent().getStringArrayListExtra("lista_numeri");
        posizione = getIntent().getIntExtra("posizione", 0);
        contatti = createContactsList(contatti, listaNomi, listaNumeri);

        showListFragment = new ShowListFragment(contatti, posizione);
        budgetFragment = new BudgetFragment();
        searchFragment = new SearchFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, showListFragment).commit();

        bottomNav = findViewById(R.id.btn_nav);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.menu_list:
                        selectedFragment = showListFragment;
                        break;
                    case R.id.menu_budget:
                        selectedFragment = budgetFragment;
                        break;
                    case R.id.menu_search:
                        selectedFragment = searchFragment;
                        break;
                }
                if(selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }
                return true;
            }
        });

        getSupportActionBar().setTitle(getIntent().getStringExtra("nome"));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

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
}