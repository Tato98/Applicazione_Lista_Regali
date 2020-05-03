package com.example.applicazione_lista_regali.Fragments;

import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicazione_lista_regali.Models.Contatti;
import com.example.applicazione_lista_regali.R;
import com.example.applicazione_lista_regali.Utilities.ContactGiftAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.tooltip.Tooltip;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ShowListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ContactGiftAdapter contactGiftAdapter;
    private ArrayList<Contatti> contacts;
    private ImageButton addPerson;
    private Tooltip hintImportContacts;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_list_fragment, container, false);

        addPerson = view.findViewById(R.id.add_person);

        ArrayList<String> listaNomi = getActivity().getIntent().getStringArrayListExtra("lista_nomi");
        ArrayList<String> listaNumeri = getActivity().getIntent().getStringArrayListExtra("lista_numeri");
        contacts = new ArrayList<>();

        createContactsList(contacts, listaNomi, listaNumeri);

        initRecyclerView(view);

        tooltipBuild();
        if(contacts.isEmpty())
            hintImportContacts.show();
        else
            hintImportContacts.dismiss();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    Contatti deleteContact = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();

            switch(direction) {
                case ItemTouchHelper.LEFT: {
                    deleteContact = contacts.get(position);
                    contacts.remove(position);
                    contactGiftAdapter.notifyItemRemoved(position);
                    Snackbar.make(recyclerView, deleteContact.getNome(), Snackbar.LENGTH_LONG).setAction("Indietro", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            contacts.add(position, deleteContact);
                            contactGiftAdapter.notifyItemInserted(position);
                        }
                    }).show();
                    break;
                }
                case ItemTouchHelper.RIGHT: {

                }
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            new RecyclerViewSwipeDecorator.Builder(getActivity(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getActivity(), R.color.remove_color))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_sweep)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getActivity(), R.color.edit_color))
                    .addSwipeRightActionIcon(R.drawable.ic_edit)
                    .create()
                    .decorate();
        }


    };

    //crea una lista di contatti con i valori contenuti nelle due liste di stringhe
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

    public void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.lista_regali);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        contactGiftAdapter = new ContactGiftAdapter(contacts);
        recyclerView.setAdapter(contactGiftAdapter);
        contactGiftAdapter.notifyDataSetChanged();
    }

    public void tooltipBuild() {
        hintImportContacts = new Tooltip.Builder(addPerson)
                .setText(R.string.hint_insert_contact)
                .setCornerRadius(50f)
                .setGravity(Gravity.TOP)
                .setTextSize(15f)
                .setArrowHeight(80f)
                .setPadding(20f)
                .setArrowWidth(30f)
                .setTypeface(Typeface.DEFAULT)
                .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryTrasparent))
                .build();
    }
}
