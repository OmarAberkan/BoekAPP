package com.example.omar.apvalley;

/**
 * Created by Omar on 29/10/2017.
 */

public class Boeken {

    public String foto;
    public String titel;
    public String auteur;
    public String uitgeverij;
    public String datum;
    public String ISBN;
    public Double prijs;
    public String userId;
    public String richting;
    public Boeken() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Boeken(String foto,String titel, String auteur,String uitgeverij,String datum, String ISBN,Double prijs,String userId,String richting) {
        this.richting=richting;
        this.foto=foto;
        this.titel=titel;
        this.auteur=auteur;
        this.uitgeverij=uitgeverij;
        this.datum=datum;
        this.ISBN=ISBN;
        this.prijs=prijs;
        this.userId=userId;

    }
    public Boeken(String foto,String titel, String auteur,String uitgeverij,String datum, String ISBN,Double prijs,String userId) {

        this.foto=foto;
        this.titel=titel;
        this.auteur=auteur;
        this.uitgeverij=uitgeverij;
        this.datum=datum;
        this.ISBN=ISBN;
        this.prijs=prijs;
        this.userId=userId;

    }
}
