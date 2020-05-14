package com.example.applicazione_lista_regali.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.example.applicazione_lista_regali.Models.ListaRegali;
import com.example.applicazione_lista_regali.Models.Regalo;
import com.example.applicazione_lista_regali.R;
import com.example.applicazione_lista_regali.SelectedContactsActivity;
import com.example.applicazione_lista_regali.Utilities.ContactGiftAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class ShowListFragment extends Fragment implements ContactGiftAdapter.Notify {

    private static final int PICK_CONTACT = 102;
    private static final int DIALOG_FRAGMENT = 1;

    private RecyclerView recyclerView;
    private ContactGiftAdapter contactGiftAdapter;
    private ArrayList<Contatti> contacts;
    private ArrayList<ListaRegali> lista;
    private ImageButton addPerson;
    private ArrayList<String> contactNameList;
    private Contatti deleteContact = null;
    private Intent updateIntent = new Intent();
    private String budget;
    private int posizione;

    public ShowListFragment(ArrayList<ListaRegali> lista, int posizione) {
        this.lista = lista;
        this.posizione = posizione;
        this.contacts = lista.get(posizione).getContatti();
        this.budget = lista.get(posizione).getBudget();
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
                }
                break;
            }
            case DIALOG_FRAGMENT: {
                if(resultCode == RESULT_OK) {
                    Regalo regalo = intent.getParcelableExtra("regalo");
                    int posizione = intent.getIntExtra("posizione", 0);
                    contacts.get(posizione).getRegali().add(regalo);
                    contactGiftAdapter.notifyItemChanged(posizione);
                    Update();
                }
                break;
            }
        }
    }

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
                    .addSwipeRightActionIcon(R.drawable.ic_image_gift)
                    .create()
                    .decorate();
        }
    };

    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.lista_regali);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        contactGiftAdapter = new ContactGiftAdapter(contacts, getContext(), ShowListFragment.this, this);
        recyclerView.setAdapter(contactGiftAdapter);
    }

    private void Update() {
        updateIntent.putExtra("contatti_aggiornati", contacts);
        updateIntent.putExtra("posizione", posizione);
        getActivity().setResult(RESULT_OK, updateIntent);
    }

    @Override
    public void notifyUpdate() {
        Update();
    }

    @Override
    public void onStop() {
        super.onStop();
        lista.get(posizione).setContatti(contacts);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(lista);
        editor.putString("task list", json);
        editor.apply();
    }
}
