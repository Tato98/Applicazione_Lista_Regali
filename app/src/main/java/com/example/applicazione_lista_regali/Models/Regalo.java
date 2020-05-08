package com.example.applicazione_lista_regali.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Regalo implements Parcelable {

    private String nome;
    private String prezzo;

    public Regalo(String nome, String prezzo) {
        this.nome = nome;
        this.prezzo = prezzo;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(prezzo);
    }
}
