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
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ListActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private ShowListFragment showListFragment;
    private BudgetFragment budgetFragment;
    private SearchFragment searchFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        showListFragment = new ShowListFragment();
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
}