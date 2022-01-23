package com.example.faster.Data;

public class Element {
    private int id_element;
    private String intitule;
    private int nbHeur;
    private int totaleh;

    public Element() {
    }

    public Element(int id_element, String intitule, int nbHeur, int totaleh) {
        this.id_element = id_element;
        this.intitule = intitule;
        this.nbHeur = nbHeur;
        this.totaleh = totaleh;
    }

    public int getId_element() {
        return id_element;
    }

    public void setId_element(int id_element) {
        this.id_element = id_element;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public int getNbHeur() {
        return nbHeur;
    }

    public void setNbHeur(int nbHeur) {
        this.nbHeur = nbHeur;
    }

    public int getTotaleh() {
        return totaleh;
    }

    public void setTotaleh(int totaleh) {
        this.totaleh = totaleh;
    }
}
