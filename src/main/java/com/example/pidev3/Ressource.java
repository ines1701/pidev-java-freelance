package com.example.pidev3;

public class Ressource {
    private int id;
    private String titre, description,file;
    private int formation_id;

    public int getFormation_id() {
        return formation_id;
    }

    public Ressource( String titre, String description, String file, int formation_id) {

        this.titre = titre;
        this.description = description;
        this.file = file;
        this.formation_id = formation_id;
    }

    public void setFormation_id(int formation_id) {
        this.formation_id = formation_id;

    }

    public Ressource() {
    }

    public Ressource(String file) {
        this.file = file;
    }


    public Ressource( String titre, String description, String file) {

        this.titre = titre;
        this.description = description;
        this.file = file;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "Ressource{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", file='" + file + '\'' +
                '}';
    }


}
