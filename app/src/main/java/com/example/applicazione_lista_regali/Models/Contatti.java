package com.example.applicazione_lista_regali.Models;

import java.util.ArrayList;

public class Contatti {

    private String nome;
    private String numero;
    private String id;

    public Contatti(String nome, String numero) {
        this.nome = nome;
        this.numero = numero;
    }

    public Contatti(String nome, String numero, String id) {
        this.nome = nome;
        this.numero = numero;
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getNumero() {
        return numero;
    }

    public String getId() {
        return id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setId(String id) {
        this.id = id;
    }
}
