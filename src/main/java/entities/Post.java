
package entities;

import java.time.LocalDate;


public class Post {
    private Long id;
    private String description;
    private LocalDate datePublication;
    private String nom;
    private Groupe groupe;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(LocalDate datePublication) {
        this.datePublication = datePublication;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Groupe getGroupe() {
        return groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Post {")
                .append("\n   ID: ").append(id)
                .append("\n   Description: ").append(description)
                .append("\n   Date de publication: ").append(datePublication)
                .append("\n   Nom: ").append(nom)
                .append("\n   Groupe: ").append(groupe != null ? groupe.getNom() : "Aucun groupe")
                .append("}");

        return stringBuilder.toString();
    }




}
