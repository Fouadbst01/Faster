package com.example.faster.Data;

public class Compte {
    private int id_cmpt;
    private String username;
    private String pswd;

    public Compte(){

    }

    public Compte(int id_cmpt, String username, String pswd) {
        this.id_cmpt = id_cmpt;
        this.username = username;
        this.pswd = pswd;
    }

    public int getId_cmpt() {
        return id_cmpt;
    }

    public void setId_cmpt(int id_cmpt) {
        this.id_cmpt = id_cmpt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }
}
