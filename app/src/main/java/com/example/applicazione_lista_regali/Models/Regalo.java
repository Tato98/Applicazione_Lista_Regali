package com.example.applicazione_lista_regali.Models;

public class Regalo {

    private String nome;
    private String prezzo;

    public Regalo(String nome, String prezzo) {
        this.nome = nome;
        this.prezzo = prezzo;
    }

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
}
