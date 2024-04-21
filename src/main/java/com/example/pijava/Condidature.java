package com.example.pijava;

public class Condidature {
    private int id, num_tel, project_id;
    private String name, prenom, email, lettredemotivation,cv, status = "En cours";
    private Project project;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum_tel() {
        return num_tel;
    }

    public void setNum_tel(int num_tel) {
        this.num_tel = num_tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLettredemotivation() {
        return lettredemotivation;
    }

    public void setLettredemotivation(String lettredemotivation) {
        this.lettredemotivation = lettredemotivation;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public Condidature(int id, int num_tel, String name, String prenom, String email, String lettredemotivation, String cv, String status) {
        this.id = id;
        this.num_tel = num_tel;
        this.name = name;
        this.prenom = prenom;
        this.email = email;
        this.lettredemotivation = lettredemotivation;
        this.cv = cv;
        this.status = status;
    }

    public Condidature() {
        this.name = name;
        this.prenom = prenom;
        this.email = email;
        this.num_tel = num_tel;
        this.lettredemotivation = lettredemotivation;
        this.cv = cv;
        this.status = status;
        this.id = id;
    }

    public Condidature(String name, String prenom, String email, int num_tel, String lettredemotivation, String cv, int project_id) {
        this.name = name;
        this.prenom = prenom;
        this.email = email;
        this.num_tel = num_tel;
        this.lettredemotivation = lettredemotivation;
        this.cv = cv;
        this.project_id = project_id;
    }

}
