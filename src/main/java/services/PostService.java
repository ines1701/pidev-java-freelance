/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import entities.Groupe;
import entities.Post;
import utilities.Myconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class PostService implements CrudInterface<Post> {

    private Connection cnx;

    public PostService() {
        cnx = Myconnection.getInstance().getCnx();
    }

    @Override
    public void create(Post post) {
        String query = "INSERT INTO Post (description, date_publication, nom, groupe_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, post.getDescription());

            String formattedDate = post.getDatePublication().toString();
            pst.setString(2, formattedDate);

            pst.setString(3, post.getNom());
            pst.setLong(4, post.getGroupe().getId());

            pst.executeUpdate();
            System.out.println("Post ajouté avec succès");
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'ajout du post : " + ex.getMessage());
        }
    }


    @Override
    public void update(Post post) {
        if (post != null && post.getId() != null) {
            if (post.getDescription() == null || post.getDatePublication() == null || post.getNom() == null) {
                System.err.println("Some essential attributes of the post are null. Unable to update.");
                return;
            }

            String query = "UPDATE Post SET description=?, date_publication=?, nom=? WHERE id=?";
            try (PreparedStatement pst = cnx.prepareStatement(query)) {
                pst.setString(1, post.getDescription());
                pst.setString(2, post.getDatePublication().toString()); // Ensure correct date format
                pst.setString(3, post.getNom());
                pst.setLong(4, post.getId());

                int rowsUpdated = pst.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Post updated successfully");
                } else {
                    System.out.println("No post found with ID: " + post.getId());
                }
            } catch (SQLException ex) {
                System.err.println("Error updating the post: " + ex.getMessage());
            }
        } else {
            System.err.println("The post object or its essential attributes are null. Unable to update.");
        }
    }


    @Override
    public void delete(int id) {
        String query = "DELETE FROM Post WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, id);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Post supprimé avec succès");
            } else {
                System.out.println("Aucun post trouvé avec l'ID : " + id);
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la suppression du post : " + ex.getMessage());
        }
    }

    @Override
    public Post getById(int id) {
        String query = "SELECT p.*, g.nom AS groupe_nom FROM Post p JOIN Groupe g ON p.groupe_id = g.id WHERE p.id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Post post = new Post();
                    post.setId(rs.getLong("id"));
                    post.setDescription(rs.getString("description"));

                    // Convert the date from string to LocalDate
                    String dateAsString = rs.getString("date_publication");
                    LocalDate datePublication = LocalDate.parse(dateAsString);
                    post.setDatePublication(datePublication);

                    post.setNom(rs.getString("nom"));

                    // Set the group name
                    String groupeNom = rs.getString("groupe_nom");
                    Groupe groupe = new Groupe();
                    groupe.setNom(groupeNom);
                    post.setGroupe(groupe);

                    return post;
                } else {
                    System.out.println("Aucun post trouvé avec l'ID : " + id);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération du post : " + ex.getMessage());
        }
        return null;
    }



    @Override
    public List<Post> getAll() {
        List<Post> postList = new ArrayList<>();
        String query = "SELECT p.*, g.nom AS groupe_nom FROM Post p LEFT JOIN Groupe g ON p.groupe_id = g.id";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Post post = new Post();
                    post.setId(rs.getLong("id"));
                    post.setDescription(rs.getString("description"));

                    // Convert the date from string to LocalDate
                    String dateAsString = rs.getString("date_publication");
                    LocalDate datePublication = LocalDate.parse(dateAsString);
                    post.setDatePublication(datePublication);

                    post.setNom(rs.getString("nom"));

                    String groupeNom = rs.getString("groupe_nom");
                    if (groupeNom != null && !groupeNom.isEmpty()) {
                        Groupe groupe = new Groupe();
                        groupe.setNom(groupeNom);
                        post.setGroupe(groupe);
                    }

                    postList.add(post);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error retrieving posts: " + ex.getMessage());
        }
        return postList;
    }
    public Month getMostCommonMonth() {
        String query = "SELECT MONTH(date_publication) AS month, COUNT(*) AS count " +
                "FROM Post " +
                "GROUP BY MONTH(date_publication) " +
                "ORDER BY count DESC " +
                "LIMIT 1";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int monthNumber = rs.getInt("month");
                    return Month.of(monthNumber);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error retrieving most common month: " + ex.getMessage());
        }

        return null;
    }
    public List<Post> searchByNom(String nom) {
        List<Post> postList = new ArrayList<>();
        String query = "SELECT p.*, g.nom AS groupe_nom FROM Post p " +
                "LEFT JOIN Groupe g ON p.groupe_id = g.id " +
                "WHERE p.nom LIKE ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            // Utiliser LIKE avec % pour rechercher les noms qui contiennent la chaîne donnée
            pst.setString(1, "%" + nom + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Post post = new Post();
                    post.setId(rs.getLong("id"));
                    post.setDescription(rs.getString("description"));

                    // Convertir la date de string en LocalDate
                    String dateAsString = rs.getString("date_publication");
                    LocalDate datePublication = LocalDate.parse(dateAsString);
                    post.setDatePublication(datePublication);

                    post.setNom(rs.getString("nom"));

                    String groupeNom = rs.getString("groupe_nom");
                    if (groupeNom != null && !groupeNom.isEmpty()) {
                        Groupe groupe = new Groupe();
                        groupe.setNom(groupeNom);
                        post.setGroupe(groupe);
                    }

                    postList.add(post);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la recherche des publications par nom : " + ex.getMessage());
        }
        return postList;
    }
}
