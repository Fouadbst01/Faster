package com.example.faster.Data;

public class Professeur {
    private int id_prof;
    private String nom;
    private String prenom;
    private String cin;
    private int id_cmpt;
    private int id_dept;
    public Professeur(){

    }
    public Professeur(int id_prof, String nom, String prenom, String cin, int id_cmpt, int id_dept) {
        this.id_prof = id_prof;
        this.nom = nom;
        this.prenom = prenom;
        this.cin = cin;
        this.id_cmpt = id_cmpt;
        this.id_dept = id_dept;
    }

    public int getId_prof() {
        return id_prof;
    }

    public void setId_prof(int id_prof) {
        this.id_prof = id_prof;
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

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public int getId_cmpt() {
        return id_cmpt;
    }

    public void setId_cmpt(int id_cmpt) {
        this.id_cmpt = id_cmpt;
    }

    public int getId_dept() {
        return id_dept;
    }

    public void setId_dept(int id_dept) {
        this.id_dept = id_dept;
    }
}
