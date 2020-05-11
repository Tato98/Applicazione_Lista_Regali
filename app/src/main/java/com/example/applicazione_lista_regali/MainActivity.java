package com.example.applicazione_lista_regali;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicazione_lista_regali.Fragments.ModifyDialog;
import com.example.applicazione_lista_regali.Models.Contatti;
import com.example.applicazione_lista_regali.Models.ListaRegali;
import com.example.applicazione_lista_regali.Utilities.ListAdapter;
import com.tooltip.Tooltip;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ListAdapter.OnListListener, ModifyDialog.OnSendData {

    public static final int CREATE_REQUEST = 101;
    public static final int OPEN_REQUEST = 102;

    private Button addList;
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private ListaRegali listaRegali;
    private ArrayList<ListaRegali> lista = new ArrayList<>();
    private ArrayList<String> nomiListe;
    private Tooltip hintListCreation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addList = findViewById(R.id.addList);
        tooltipBuild();
        hintListCreation.show();

        initRecyclerView();

        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListCreationActivity.class);
                nomiListe = new ArrayList<>();
                for (ListaRegali lr: lista) {
                    nomiListe.add(lr.getNome());
                }
                intent.putStringArrayListExtra("nomi", nomiListe);
                startActivityForResult(intent, CREATE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode) {
            case CREATE_REQUEST: {

                if(resultCode == RESULT_OK) {
                    listaRegali = intent.getParcelableExtra("lista_regali");
                    lista.add(listaRegali);

                    listAdapter.notifyDataSetChanged();

                    if(lista.isEmpty())
                        hintListCreation.show();
                    else
                        hintListCreation.dismiss();
                }
                break;
            }

            case OPEN_REQUEST: {

                if(resultCode == RESULT_OK) {
                    int posizione = intent.getIntExtra("posizione", 0);
                    ArrayList<Contatti> contattiAggiornati = intent.getParcelableArrayListExtra("contatti_aggiornati");
                    lista.get(posizione).setContatti(contattiAggiornati);
                }
                break;
            }
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(linearLayoutManager);
        listAdapter = new ListAdapter(lista, this);
        recyclerView.setAdapter(listAdapter);
    }

    @Override
    public void OnListClick(int position) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("Lista", lista.get(position));
        intent.putExtra("posizione", position);
        startActivityForResult(intent, OPEN_REQUEST);
    }

    @Override
    public void OnListLongClick(final int position, View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.list_option_menu, popup.getMenu());
        popup.setGravity(Gravity.END);
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.option1:
                        Bundle bundle = new Bundle();
                        nomiListe = new ArrayList<>();
                        for (ListaRegali lr: lista) {
                            nomiListe.add(lr.getNome());
                        }
                        bundle.putInt("posizione", position);
                        bundle.putStringArrayList("nomi_liste", nomiListe);

                        ModifyDialog fragment_modifica_dialog = new ModifyDialog(MainActivity.this);
                        fragment_modifica_dialog.setArguments(bundle);
                        fragment_modifica_dialog.show(getSupportFragmentManager(),"DialogFragment");
                        return true;
                    case R.id.option2:
                        lista.remove(lista.get(position));
                        listAdapter.notifyItemRemoved(position);
                        return true;
                }
                return false;
            }
        });
    }

    public void tooltipBuild() {
        hintListCreation = new Tooltip.Builder(addList)
                .setText(R.string.hint_list_creation)
                .setCornerRadius(50f)
                .setGravity(Gravity.TOP)
                .setTextSize(15f)
                .setArrowHeight(100f)
                .setPadding(40f)
                .setArrowWidth(50f)
                .setTypeface(Typeface.DEFAULT)
                .setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryTrasparent))
                .build();
    }

    @Override
    public void OnReceiveData(String newName, String newDescription, int position) {
        lista.get(position).setNome(newName);
        lista.get(position).setDescrizione(newDescription);
        listAdapter.notifyItemChanged(position);
    }
}
