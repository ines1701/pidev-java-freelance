package com.example.test1;


import java.sql.Date;
import java.sql.Timestamp;

public class Event {

    private int id;
    private String titre;
    private String describ;
    private String lieu;

    private Timestamp date;

    public Event() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescrib() {
        return describ;
    }

    public void setDescrib(String describ) {
        this.describ = describ;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
    // Modifiez le constructeur pour inclure le nouvel attribut
    public Event(String titre, String describ, String lieu, Timestamp date) {
        this.titre = titre;
        this.describ = describ;
        this.lieu = lieu;
        this.date = date;
    }


    @Override
    public String toString() {
        return "Event{" +
                "titre='" + titre + '\'' +
                ", describ='" + describ + '\'' +
                ", lieu='" + lieu + '\'' +
                ", date='" + date + '\'' + // Incluez le nouvel attribut dans la chaîne de caractères de sortie
                '}';
    }


    public void setDate(Date sqlDate) {
    }
}
