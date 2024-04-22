/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hassa
 */
public class Groupe {
    private Long id;
    private String groupe;
    private String description;
    private LocalDate dateDeCreation;
    private String nom;
    private List<Post> posts = new ArrayList<>();
    private String image;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupe() {
        return groupe;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateDeCreation() {
        return dateDeCreation;
    }

    public void setDateDeCreation(LocalDate dateDeCreation) {
        this.dateDeCreation = dateDeCreation;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void addPost(Post post) {
        if (!this.posts.contains(post)) {
            this.posts.add(post);
            post.setGroupe(this);
        }
    }

    public void removePost(Post post) {
        if (this.posts.contains(post)) {
            this.posts.remove(post);
            post.setGroupe(null);
        }
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Groupe {")
                .append("\n   ID: ").append(id)
                .append("\n   Groupe: ").append(groupe)
                .append("\n   Description: ").append(description)
                .append("\n   Date de création: ").append(dateDeCreation)
                .append("\n   Nom: ").append(nom)
                .append("\n   Image: ").append(image)
                .append("\n   Posts: ");

        if (posts.isEmpty()) {
            stringBuilder.append("No posts");
        } else {
            stringBuilder.append("\n");
            for (Post post : posts) {
                stringBuilder.append("      ").append(post).append("\n");
            }
        }
        stringBuilder.append("}");

        return stringBuilder.toString();
    }

}
