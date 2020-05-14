package com.example.applicazione_lista_regali.Utilities;

import android.os.Environment;

import com.example.applicazione_lista_regali.Models.ListaRegali;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class DataManager implements Serializable {

    private ListaRegali listaRegali;
    private ArrayList<ListaRegali> lista;

    public DataManager(ListaRegali listaRegali, ArrayList<ListaRegali> lista) {
        this.listaRegali = listaRegali;
        this.lista = lista;
    }

    public void saveData(ListaRegali listaRegali) throws FileNotFoundException, IOException {
        try {
            File file = new File(Environment.getDataDirectory(), "/data/data/com.example.applicazione_lista_regali/NewTextFile.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
            outputStream.writeObject(listaRegali);
            outputStream.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ListaRegali> loadData() {
        ObjectInput objectInput;
        this.lista = new ArrayList<>();
        try {
            objectInput = new ObjectInputStream(new FileInputStream("/data/data/com.example.applicazione_lista_regali/NewTextFile.txt"));
            lista = (ArrayList<ListaRegali>) objectInput.readObject();
            objectInput.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
