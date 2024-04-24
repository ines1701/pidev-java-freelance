package com.example.pidev3;

public class Formation { private int id;
    private String titre, categorie,tuteur,updated;
    private boolean favoris;

    public Formation(int id, String titre, String categorie, String tuteur, String updated, boolean favoris) {
        this.id = id;
        this.titre = titre;
        this.categorie = categorie;
        this.tuteur = tuteur;
        this.updated = updated;
        this.favoris = favoris;
    }

    public Formation(String titre, String categorie, String tuteur, String updated, boolean favoris) {

        this.titre = titre;
        this.categorie = categorie;
        this.tuteur = tuteur;
        this.updated = updated;
        this.favoris = favoris;
    }

    public boolean isFavoris() {
        return favoris;
    }

    public void setFavoris(boolean favoris) {
        this.favoris = favoris;
    }

    public Formation() {
    }

    public Formation(String titre, String categorie, String tuteur, String updated) {

        this.titre = titre;
        this.categorie = categorie;
        this.tuteur = tuteur;
        this.updated = updated;
    }


    public Formation(int id, String titre, String categorie, String tuteur, String updated) {
        this.id = id;
        this.titre = titre;
        this.categorie = categorie;
        this.tuteur = tuteur;
        this.updated = updated;
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

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getTuteur() {
        return tuteur;
    }

    public void setTuteur(String tuteur) {
        this.tuteur = tuteur;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "Formation{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", categorie='" + categorie + '\'' +
                ", tuteur='" + tuteur + '\'' +
                ", updated='" + updated + '\'' +
                '}';
    }


}
