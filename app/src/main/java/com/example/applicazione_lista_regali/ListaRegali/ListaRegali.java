package com.example.applicazione_lista_regali.ListaRegali;

public class ListaRegali {
    private String nome;
    private String descrizione;
    //private ArrayList<Object> contatti;

    public ListaRegali(String nome, String descrizione/*, ArrayList<Object> contatti*/) {
        this.nome = nome;
        this.descrizione = descrizione;
        //this.contatti = contatti;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    /*public ArrayList<Object> getContatti() {
        return contatti;
    }*/
}
