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
    public String departement;
    public String klas;
    public String username;
    public Integer aantal;

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
    public Boeken(String foto,String titel, String departement,Double prijs,String richting,String klas) {
        this.klas=klas;
        this.departement=departement;
        this.richting=richting;
        this.foto=foto;
        this.titel=titel;
        this.prijs=prijs;


    }
    public Boeken(String foto,String titel,Double prijs,String richting,String klas,String uitgeverij,String ISBN,String datum,String departement,String userId) {
        this.ISBN=ISBN;
        this.departement=departement;
        this.datum=datum;
        this.foto=foto;
        this.klas=klas;
        this.prijs=prijs;
        this.richting=richting;
        this.titel=titel;
        this.userId=userId;
        this.uitgeverij=uitgeverij;



    }

    public Boeken(String foto,String titel,String richting,String klas,String uitgeverij,String ISBN,String datum,String departement,String userId,String username) {
        this.ISBN=ISBN;
        this.departement=departement;
        this.datum=datum;
        this.foto=foto;
        this.klas=klas;

        this.richting=richting;
        this.titel=titel;
        this.userId=userId;
        this.uitgeverij=uitgeverij;
        this.username=username;

    }






    public Boeken(String foto,String titel, String auteur,String uitgeverij,String datum, String ISBN,Double prijs,String userId) {
        this.ISBN=ISBN;
        this.datum=datum;
        this.foto=foto;

        this.prijs=prijs;
        this.titel=titel;
        this.auteur=auteur;
        this.uitgeverij=uitgeverij;



        this.userId=userId;

    }

    public Boeken(String foto,String titel,String auteur,String ISBN,String departement,String richting,String klas,Integer aantal) {
        this.foto=foto;
        this.titel=titel;
        this.auteur=auteur;
        this.ISBN=ISBN;
        this.departement=departement;
        this.richting=richting;
        this.klas=klas;
        this.aantal=aantal;
    }
    public Boeken(String foto,String titel,String auteur,String ISBN,String departement,String richting,String klas) {
        this.foto=foto;
        this.titel=titel;
        this.auteur=auteur;
        this.ISBN=ISBN;
        this.departement=departement;
        this.richting=richting;
        this.klas=klas;
        this.aantal=aantal;
    }

    public Boeken(String foto,String titel,String auteur,String ISBN,String departement,String richting,String klas,String username,String userId) {
        this.foto=foto;
        this.titel=titel;
        this.auteur=auteur;
        this.ISBN=ISBN;
        this.departement=departement;
        this.richting=richting;
        this.klas=klas;
        this.username=username;
        this.userId=userId;
    }



}
