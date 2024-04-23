package com.example.pijava;

import java.time.LocalDateTime;

public class Project {
    private int id ;
    private String titre, categorie, portee, periode, description;
    private double budget;

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

    public String getPortee() {
        return portee;
    }

    public void setPortee(String portee) {
        this.portee = portee;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public Project() {
    }

    public Project(String titre, String categorie, String periode, String portee, String description, double budget) {
        this.titre = titre;
        this.categorie = categorie;
        this.periode = periode;
        this.portee = portee;
        this.description = description;
        this.budget = budget;
    }

    public Project(int id, String titre, String categorie, String periode, String portee, String description, double budget) {
        this.id = id;
        this.titre = titre;
        this.categorie = categorie;
        this.portee = portee;
        this.periode = periode;
        this.description = description;
        this.budget = budget;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id='" + id +'\'' +
                ",titre='" + titre + '\'' +
                ", categorie='" + categorie + '\'' +
                ", portee='" + portee + '\'' +
                ", periode='" + periode + '\'' +
                ", description='" + description + '\'' +
                ", budget=" + budget +
                '}';
    }
}
