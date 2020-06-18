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

//La seguente classe è un frammento di "ListActivity" che mostra e permette di gestire la lista
//di contatti. Gestisce l'eliminazione di un contatto dalla lista, l'aggiunta di un nuovo contatto,
//oppure l'aggiunta, la rimozione e la modifica di un qualsivoglia regalo di un qualsiasi contatto.
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
    private OnSendTotSpent onSendTotSpent;
    private int posizione;

    //Costruttore della classe
    public ShowListFragment(ArrayList<ListaRegali> lista, int posizione, OnSendTotSpent onSendTotSpent) {
        this.lista = lista;
        this.posizione = posizione;
        this.onSendTotSpent = onSendTotSpent;
        this.contacts = lista.get(posizione).getContatti();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_list_fragment, container, false);

        //Bottone che permette di aggiungere un nuovo contatto alla lista
        addPerson = view.findViewById(R.id.add_person);
        addPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Tramite un intent si passa alla "SelectedContactActivity" dalla quale si può
                //pescare il/i contatto/i da aggiungere alla lista
                Intent intent = new Intent(getActivity(), SelectedContactsActivity.class);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);

                //Si fornisce inoltre la lista contenente i contatti già selezionati in modo
                //che vengano poi disabilitati durante la selezione nella "SelectedContactActivity"
                contactNameList = new ArrayList<>();
                for (Contatti cnt: contacts) {
                    contactNameList.add(cnt.getNome());
                }
                intent.putStringArrayListExtra("nomi", contactNameList);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        //Chiamata del metodo che permette di inizializzare la recycler view
        initRecyclerView(view);

        //Permette di inviare alla Activity di questo frammento il totale del budget speso
        onSendTotSpent.ReceiveTotSpent(contactGiftAdapter.totSpent());

        //Le due seguenti righe di codice permettono di inizializzare lo "swipe" nella lista
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        //In base al valore di requestCode vengon effettuate determinate operazioni
        switch(requestCode) {
            //In questo caso si aggiunge il contatto scelto e inviato dalla "SelectedContactActivity" alla lista
            case PICK_CONTACT: {
                if(resultCode == RESULT_OK) {
                    ArrayList<Contatti> resultContacts = intent.getParcelableArrayListExtra("contatti_scelti");

                    contacts.addAll(resultContacts);

                    contactGiftAdapter.notifyDataSetChanged();

                    //Si aggiorna la MainActivity dei cambiamenti avvenuti
                    Update();
                }
                break;
            }
            //In questo caso si aggiorna la lista regali del contatto selezionato con l'aggiunta del regalo
            // proveniente dal frammento InsertGiftDialog
            case DIALOG_FRAGMENT: {
                if(resultCode == RESULT_OK) {
                    Regalo regalo = intent.getParcelableExtra("regalo");
                    int posizione = intent.getIntExtra("posizione", 0);
                    contacts.get(posizione).getRegali().add(regalo);
                    contactGiftAdapter.notifyItemChanged(posizione);
                    //Si aggiorna la MainActivity dei cambiamenti avvenuti e si invia il totale speso per aggiornare
                    //la sezione di controllo del budget
                    Update();
                    onSendTotSpent.ReceiveTotSpent(contactGiftAdapter.totSpent());
                }
                break;
            }
        }
    }

    //La seguente parte di codice permette di gestire lo swipe degli elementi della lista
    //__________________________________________________________________________________________________________________________________________________________________________________________________
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();

            //Controllo direzione swipe
            switch(direction) {
                //Caso in cui si effettui lo swipe da destra verso sinistra: viene gestita la rimozione di un contatto
                case ItemTouchHelper.LEFT: {
                    //Eliminato il contatto lo si salva in deleteContact affinchè possa essere recuperato
                    deleteContact = contacts.get(position);
                    contacts.remove(position);
                    contactGiftAdapter.notifyItemRemoved(position);
                    Update();
                    onSendTotSpent.ReceiveTotSpent(contactGiftAdapter.totSpent());

                    //Il seguente Snackbar compare se si elimina un contatto e permette di cancellare l'operazione cliccando su indietro
                    Snackbar.make(recyclerView, deleteContact.getNome(), Snackbar.LENGTH_LONG).setAction("Indietro", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Se si anulla la cancellazione si riaggiunge il contatto salvato in deleteContact alla lista
                            contacts.add(position, deleteContact);
                            contactGiftAdapter.notifyItemInserted(position);
                            Update();
                            onSendTotSpent.ReceiveTotSpent(contactGiftAdapter.totSpent());
                        }
                    }).show();
                    break;
                }
                //Caso swipe da sinistra verso destra: viene fatto partire il dialog per l'aggiunta del regalo
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


        //Gestisce il layout dello swipe
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
    //__________________________________________________________________________________________________________________________________________________________________________________________________

    //Il seguente metodo permette di inizializzare la recycler view della lista
    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.lista_regali);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        contactGiftAdapter = new ContactGiftAdapter(contacts, getContext(), ShowListFragment.this, this);
        recyclerView.setAdapter(contactGiftAdapter);
    }

    //Metodo che permette di inviare alla MainActivity tutte le modifiche effettuate alla lista
    private void Update() {
        updateIntent.putExtra("contatti_aggiornati", contacts);
        updateIntent.putExtra("posizione", posizione);
        getActivity().setResult(RESULT_OK, updateIntent);
    }

    //Ovverride del metodo dell'interfaccia Notify di ContactGiftAdapter
    @Override
    public void notifyUpdate() {
        Update();
        //Invia all'activity di questo frammento il totale speso per aggiornare la sezione di controllo del budget
        onSendTotSpent.ReceiveTotSpent(contactGiftAdapter.totSpent());
    }

    //Qualora l'app venisse chiusa improvvisamente dall'utente verrebbero salvate tutte le modifiche nelle shared preferences
    //utilizzate come database dell'app e contenenti tutte le liste regali
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

    //Interfaccia utilizzata per inviare all'activity di questo frammento il totale speso
    public interface OnSendTotSpent {
        void ReceiveTotSpent(double totSpent);
    }
}
