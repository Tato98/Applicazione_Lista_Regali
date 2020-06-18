package com.example.applicazione_lista_regali.Models;

import android.os.Parcel;
import android.os.Parcelable;

//Modello rappresentante il regalo
public class  Regalo implements Parcelable {

    private String nome;    //nome regalo
    private String prezzo;  //prezzo regalo

    public Regalo(String nome, String prezzo) {
        this.nome = nome;
        this.prezzo = prezzo;
    }

    //Costruttore Parcelable
    protected Regalo(Parcel in) {
        nome = in.readString();
        prezzo = in.readString();
    }

    public static final Creator<Regalo> CREATOR = new Creator<Regalo>() {
        @Override
        public Regalo createFromParcel(Parcel in) {
            return new Regalo(in);
        }

        @Override
        public Regalo[] newArray(int size) {
            return new Regalo[size];
        }
    };

    //_______________________________ Metodi Get e Set ____________________________________________
    public String getNome() {
        return nome;
    }

    public String getPrezzo() {
        return prezzo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPrezzo(String prezzo) {
        this.prezzo = prezzo;
    }
    //_____________________________________________________________________________________________


    //________________________________ Metodi Parcelable __________________________________________
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(prezzo);
    }
    //_____________________________________________________________________________________________
}
