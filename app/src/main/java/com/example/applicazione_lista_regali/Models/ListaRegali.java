package com.example.applicazione_lista_regali.Models;

import java.util.ArrayList;

public class ListaRegali {
    private String nome;
    private String descrizione;
    private ArrayList<Contatti> contatti;

    public ListaRegali(String nome, String descrizione, ArrayList<Contatti> contatti) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.contatti = contatti;
    }

    //Restituisce la lista dei nomi dei contatti presenti nell'ArrayList<Contatti> contatti
    public ArrayList<String> getContactsName(ArrayList<Contatti> contatti) {
        ArrayList<String> nameList = new ArrayList<>();
        for (Contatti contactName: contatti) {
            nameList.add(contactName.getNome());
        }
        return nameList;
    }

    //Restituisce la lista dei numeri dei contatti presenti nell'ArrayList<Contatti> contatti
    public ArrayList<String> getContactsNumber(ArrayList<Contatti> contatti) {
        ArrayList<String> numberList = new ArrayList<>();
        for (Contatti contactNumber: contatti) {
            numberList.add(contactNumber.getNumero());
        }
        return numberList;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public ArrayList<Contatti> getContatti() {
        return contatti;
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
}
