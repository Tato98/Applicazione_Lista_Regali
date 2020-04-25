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
