package com.example.applicazione_lista_regali.Fragments;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.example.applicazione_lista_regali.Models.Regalo;
import com.example.applicazione_lista_regali.R;
import com.example.applicazione_lista_regali.SelectedContactsActivity;
import com.example.applicazione_lista_regali.Utilities.ContactGiftAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.tooltip.Tooltip;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import static android.app.Activity.RESULT_OK;

public class ShowListFragment extends Fragment {

    private static final int PICK_CONTACT = 102;
    public static final int DIALOG_FRAGMENT = 1;

    private RecyclerView recyclerView;
    private ContactGiftAdapter contactGiftAdapter;
    private ArrayList<Contatti> contacts;
    private ImageButton addPerson;
    private Tooltip hintImportContacts;
    private ArrayList<String> contactNameList;
    private Intent updateIntent = new Intent();
    private int posizione;

    public ShowListFragment(ArrayList<Contatti> contatti, int posizione) {
        this.contacts = contatti;
        this.posizione = posizione;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_list_fragment, container, false);

        addPerson = view.findViewById(R.id.add_person);
        addPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectedContactsActivity.class);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                contactNameList = new ArrayList<>();
                for (Contatti cnt: contacts) {
                    contactNameList.add(cnt.getNome());
                }
                intent.putStringArrayListExtra("nomi", contactNameList);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode) {
            case PICK_CONTACT: {
                if(resultCode == RESULT_OK) {
                    ArrayList<Contatti> resultContacts = intent.getParcelableArrayListExtra("contatti_scelti");

                    contacts.addAll(resultContacts);

                    contactGiftAdapter.notifyDataSetChanged();

                    Update();

                    if(contacts.isEmpty())
                        hintImportContacts.show();
                    else
                        hintImportContacts.dismiss();
                }
                break;
            }
            case DIALOG_FRAGMENT: {
                if(resultCode == RESULT_OK) {
                    String nomeRegalo = intent.getStringExtra("nome_regalo");
                    String prezzoRegalo = intent.getStringExtra("prezzo");
                    int posizione = intent.getIntExtra("posizione", 0);
                    contacts.get(posizione).setRegalo(new Regalo(nomeRegalo, prezzoRegalo));
                    contactGiftAdapter.notifyItemChanged(posizione);
                    Update();
                }
                break;
            }
        }
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
                    Update();
                    Snackbar.make(recyclerView, deleteContact.getNome(), Snackbar.LENGTH_LONG).setAction("Indietro", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            contacts.add(position, deleteContact);
                            contactGiftAdapter.notifyItemInserted(position);
                            Update();

                        }
                    }).show();

                    break;
                }
                case ItemTouchHelper.RIGHT: {
                    Bundle bundle = new Bundle();
                    bundle.putInt("posizione", position);
                    InsertGiftDialog insertGiftDialog = new InsertGiftDialog();
                    insertGiftDialog.setTargetFragment(ShowListFragment.this, DIALOG_FRAGMENT);
                    insertGiftDialog.setArguments(bundle);
                    insertGiftDialog.show(getFragmentManager(), "InsertGiftDialog");
                    contactGiftAdapter.notifyItemChanged(position);
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

    public void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.lista_regali);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        contactGiftAdapter = new ContactGiftAdapter(contacts);
        recyclerView.setAdapter(contactGiftAdapter);
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

    public void Update() {
        updateIntent.putExtra("contatti_aggiornati", contacts);
        updateIntent.putExtra("posizione", posizione);
        getActivity().setResult(RESULT_OK, updateIntent);
    }
}
