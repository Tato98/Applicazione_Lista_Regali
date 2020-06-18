package com.example.applicazione_lista_regali.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

//Modello rappresentante la lista regali
public class ListaRegali implements Parcelable {
    private String nome;                        //nome lista
    private String descrizione;                 //descrizione lista
    private String budget;                      //budget lista
    private ArrayList<Contatti> contatti;       //lista contatti

    public ListaRegali(String nome, String descrizione, ArrayList<Contatti> contatti, String budget) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.contatti = contatti;
        this.budget = budget;
    }

    //Costruttore Parcelable
    protected ListaRegali(Parcel in) {
        nome = in.readString();
        descrizione = in.readString();
        budget = in.readString();
        contatti = in.createTypedArrayList(Contatti.CREATOR);
    }

    public static final Creator<ListaRegali> CREATOR = new Creator<ListaRegali>() {
        @Override
        public ListaRegali createFromParcel(Parcel in) {
            return new ListaRegali(in);
        }

        @Override
        public ListaRegali[] newArray(int size) {
            return new ListaRegali[size];
        }
    };

    //______________________________ Metodi Get e Set _____________________________________________
    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public ArrayList<Contatti> getContatti() {
        return contatti;
    }

    public String getBudget() {
        return budget;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setContatti(ArrayList<Contatti> contatti) {
        this.contatti = contatti;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }
//_________________________________________________________________________________________________


    //______________________________ Metodi Parcelable ____________________________________________
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(descrizione);
        dest.writeString(budget);
        dest.writeTypedList(contatti);
    }
    //_____________________________________________________________________________________________
}
