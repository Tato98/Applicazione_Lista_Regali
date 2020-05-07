package com.example.applicazione_lista_regali.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Contatti implements Parcelable {

    private String nome;
    private String numero;
    private Regalo regalo;
    private boolean isEnable;

    public Contatti(String nome, String numero) {
        this.nome = nome;
        this.numero = numero;
    }

    public Contatti(String nome, String numero, boolean isEnable) {
        this.nome = nome;
        this.numero = numero;
        this.isEnable = isEnable;
    }

    public Contatti(String nome, String numero, Regalo regalo, boolean isEnable) {
        this.nome = nome;
        this.numero = numero;
        this.regalo = regalo;
        this.isEnable = isEnable;
    }

    protected Contatti(Parcel in) {
        nome = in.readString();
        numero = in.readString();
        isEnable = in.readByte() != 0;
    }

    public static final Creator<Contatti> CREATOR = new Creator<Contatti>() {
        @Override
        public Contatti createFromParcel(Parcel in) {
            return new Contatti(in);
        }

        @Override
        public Contatti[] newArray(int size) {
            return new Contatti[size];
        }
    };

    public String getNome() {
        return nome;
    }

    public String getNumero() {
        return numero;
    }

    public boolean getIsEnable() {
        return isEnable;
    }

    public Regalo getRegalo() {
        return regalo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setIsEnable(boolean enable) {
        isEnable = enable;
    }

    public void setRegalo(Regalo regalo) {
        this.regalo = regalo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(numero);
        dest.writeByte((byte) (isEnable ? 1 : 0));
    }
}
