package com.example.applicazione_lista_regali.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Contatti implements Parcelable {

    private String nome;
    private String numero;
    private ArrayList<Regalo> regali;
    private boolean isEnable;
    private boolean expanded;

    public Contatti(String nome, String numero, ArrayList<Regalo> regali, boolean isEnable) {
        this.nome = nome;
        this.numero = numero;
        this.regali = regali;
        this.isEnable = isEnable;
        this.expanded = false;
    }

    protected Contatti(Parcel in) {
        nome = in.readString();
        numero = in.readString();
        regali = in.createTypedArrayList(Regalo.CREATOR);
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

    public ArrayList<Regalo> getRegali() {
        return regali;
    }

    public boolean isExpanded() {
        return expanded;
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

    public void setRegali(ArrayList<Regalo> regali) {
        this.regali = regali;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(numero);
        dest.writeTypedList(regali);
        dest.writeByte((byte) (isEnable ? 1 : 0));
    }
}
