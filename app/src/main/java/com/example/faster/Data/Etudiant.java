package com.example.faster.Data;

public class Etudiant {
    private int id_etu;
    private String nom;
    private String prenom;
    private String cne;
    private int id_class;

    public Etudiant() {
    }

    public Etudiant(int id_etu, String nom, String prenom, String cne, int id_class) {
        this.id_etu = id_etu;
        this.nom = nom;
        this.prenom = prenom;
        this.cne = cne;
        this.id_class = id_class;
    }

    public int getId_etu() {
        return id_etu;
    }

    public void setId_etu(int id_etu) {
        this.id_etu = id_etu;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getCne() {
        return cne;
    }

    public void setCne(String cne) {
        this.cne = cne;
    }

    public int getId_class() {
        return id_class;
    }

    public void setId_class(int id_class) {
        this.id_class = id_class;
    }

    @Override
    public String toString() {
        return  nom + " " + prenom;
    }
}
