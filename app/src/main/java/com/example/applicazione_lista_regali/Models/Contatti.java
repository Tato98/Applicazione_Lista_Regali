package com.example.applicazione_lista_regali.Models;

public class Contatti {

    private String nome;
    private String numero;
    private String id;
    private String nomeRegalo;
    private String prezzoRegalo;
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

    public Contatti(String nome, String numero, String id) {
        this.nome = nome;
        this.numero = numero;
        this.id = id;
    }

    public Contatti(String nome, String numero, String nomeRegalo, String prezzoRegalo) {
        this.nome = nome;
        this.numero = numero;
        this.nomeRegalo = nomeRegalo;
        this.prezzoRegalo = prezzoRegalo;
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

    public boolean getIsEnable() {
        return isEnable;
    }

    public String getNomeRegalo() {
        return nomeRegalo;
    }

    public String getPrezzoRegalo() {
        return prezzoRegalo;
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

    public void setIsEnable(boolean enable) {
        isEnable = enable;
    }

    public void setNomeRegalo(String nomeRegalo) {
        this.nomeRegalo = nomeRegalo;
    }

    public void setPrezzoRegalo(String prezzoRegalo) {
        this.prezzoRegalo = prezzoRegalo;
    }
}
