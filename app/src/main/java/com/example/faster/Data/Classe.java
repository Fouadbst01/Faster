package com.example.faster.Data;

public class Classe {
    private int id_class;
    private String intitule;
    private int annee;

    public Classe() {
    }

    public Classe(int id_class, String intitule, int annee) {
        this.id_class = id_class;
        this.intitule = intitule;
        this.annee = annee;
    }

    public int getId_class() {
        return id_class;
    }

    public void setId_class(int id_class) {
        this.id_class = id_class;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }
}
