package com.example.applicazione_lista_regali.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

//Modello rappresentante i Contatti
public class Contatti implements Parcelable {

    private String nome;                //nome contatto
    private String numero;              //descrizione contatto
    private ArrayList<Regalo> regali;   //lista dei regali assegnati al contatto
    private boolean isEnable;           //variabile booleana che permette al contatto di essere abilitato o meno (usata per la selezione dei conatti)
    private boolean expanded;           //variabile booleana che gestisce l'espandibilità e la visibilità della lista regali del contatto

    public Contatti(String nome, String numero, ArrayList<Regalo> regali, boolean isEnable) {
        this.nome = nome;
        this.numero = numero;
        this.regali = regali;
        this.isEnable = isEnable;
        this.expanded = false;
    }

    //Costruttore Parcelable
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

    //________________________________ Metodi Get e Set ___________________________________________
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
    //_____________________________________________________________________________________________


    //___________________________ Metodi della classe Parcelable___________________________________
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
    //_____________________________________________________________________________________________
}
