/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import entities.Groupe;
import utilities.Myconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupeService implements CrudInterface<Groupe> {

    private Connection cnx;

    public GroupeService() {
        cnx = Myconnection.getInstance().getCnx();
    }

    @Override
    public void create(Groupe groupe) {
        String query = "INSERT INTO Groupe (groupe, description, date_de_creation, nom, image) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, groupe.getGroupe());
            pst.setString(2, groupe.getDescription());

            // Convert LocalDate to String in the format "yyyy-MM-dd"
            String formattedDate = groupe.getDateDeCreation().toString();
            pst.setString(3, formattedDate);

            pst.setString(4, groupe.getNom());
            pst.setString(5, groupe.getImage());

            pst.executeUpdate();
            System.out.println("Groupe ajouté avec succès");
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'ajout du groupe : " + ex.getMessage());
        }
    }

    @Override
    public void update(Groupe groupe) {

        String query = "UPDATE Groupe SET groupe=?, description=?, date_de_creation=?, nom=?, image=? WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, groupe.getGroupe());
            pst.setString(2, groupe.getDescription());

            // Convert LocalDate to String in the format "yyyy-MM-dd"
            String formattedDate = groupe.getDateDeCreation().toString();
            pst.setString(3, formattedDate);

            pst.setString(4, groupe.getNom());
            pst.setString(5, groupe.getImage());
            pst.setLong(6, groupe.getId());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Groupe mis à jour avec succès");
            } else {
                System.out.println("Aucun groupe trouvé avec l'ID : " + groupe.getId());
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la mise à jour du groupe : " + ex.getMessage());
        }

    }


    @Override
    public void delete(int id) {
        String query = "DELETE FROM Groupe WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, id);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Groupe supprimé avec succès");
            } else {
                System.out.println("Aucun groupe trouvé avec l'ID : " + id);
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la suppression du groupe : " + ex.getMessage());
        }
    }

    @Override
    public Groupe getById(int id) {
        String query = "SELECT * FROM Groupe WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Groupe groupe = new Groupe();
                    groupe.setId(rs.getLong("id"));
                    groupe.setGroupe(rs.getString("groupe"));
                    groupe.setDescription(rs.getString("description"));
                    java.sql.Date dateSql = rs.getDate("date_de_creation");
                    LocalDate date = dateSql.toLocalDate();
                    groupe.setDateDeCreation(date);
                    groupe.setNom(rs.getString("nom"));
                    groupe.setImage(rs.getString("image"));
                    return groupe;
                } else {
                    System.out.println("Aucun groupe trouvé avec l'ID : " + id);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération du groupe : " + ex.getMessage());
        }
        return null;
    }

    @Override
    public List<Groupe> getAll() {
        List<Groupe> groupeList = new ArrayList<>();
        String query = "SELECT * FROM Groupe";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Groupe groupe = new Groupe();
                    groupe.setId(rs.getLong("id"));
                    groupe.setGroupe(rs.getString("groupe"));
                    groupe.setDescription(rs.getString("description"));

                    // Convert java.sql.Date to LocalDate
                    java.sql.Date dateSql = rs.getDate("date_de_creation");
                    LocalDate date = dateSql.toLocalDate();
                    groupe.setDateDeCreation(date);

                    groupe.setNom(rs.getString("nom"));
                    groupe.setImage(rs.getString("image"));
                    groupeList.add(groupe);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des groupes : " + ex.getMessage());
        }
        return groupeList;
    }



    public List<Groupe> getTopActiveGroups(int limit) {
        List<Groupe> topGroups = new ArrayList<>();
        Map<Long, Integer> groupPostCounts = new HashMap<>();

        String query = "SELECT g.id AS group_id, COUNT(p.id) AS post_count " +
                "FROM Groupe g " +
                "LEFT JOIN Post p ON g.id = p.groupe_id " +
                "GROUP BY g.id " +
                "ORDER BY post_count DESC " +
                "LIMIT ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, limit);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    long groupId = rs.getLong("group_id");
                    int postCount = rs.getInt("post_count");
                    groupPostCounts.put(groupId, postCount);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error retrieving top active groups: " + ex.getMessage());
        }

        // Now retrieve the group details and set the post counts
        String groupDetailsQuery = "SELECT * FROM Groupe WHERE id IN (?)";
        try (PreparedStatement pst = cnx.prepareStatement(groupDetailsQuery)) {
            StringBuilder groupIds = new StringBuilder();
            for (long groupId : groupPostCounts.keySet()) {
                groupIds.append(groupId).append(",");
            }
            groupIds.deleteCharAt(groupIds.length() - 1); // Remove the last comma

            pst.setString(1, groupIds.toString());
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Groupe groupe = new Groupe();
                    groupe.setId(rs.getLong("id"));
                    groupe.setGroupe(rs.getString("groupe"));
                    groupe.setDescription(rs.getString("description"));
                    java.sql.Date dateSql = rs.getDate("date_de_creation");
                    LocalDate date = dateSql.toLocalDate();
                    groupe.setDateDeCreation(date);
                    groupe.setNom(rs.getString("nom"));
                    groupe.setImage(rs.getString("image"));

                    // Set the post count for this group
                    int postCount = groupPostCounts.getOrDefault(groupe.getId(), 0);
                    System.out.println(groupe.getGroupe() + " - Number of Posts: " + postCount);
                    topGroups.add(groupe);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error retrieving group details: " + ex.getMessage());
        }

        return topGroups;
    }


    public List<Groupe> search(String keyword) {
        List<Groupe> searchResults = new ArrayList<>();
        String query = "SELECT * FROM Groupe WHERE groupe LIKE ? OR nom LIKE ? OR date_de_creation = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, "%" + keyword + "%");
            pst.setString(2, "%" + keyword + "%");

            // Convert the input keyword to LocalDate if possible
            LocalDate dateKeyword = null;
            try {
                dateKeyword = LocalDate.parse(keyword);
                pst.setObject(3, dateKeyword);
            } catch (DateTimeParseException e) {
                // If the keyword is not a valid date, set it to null
                pst.setObject(3, null);
            }

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Groupe groupe = new Groupe();
                    groupe.setId(rs.getLong("id"));
                    groupe.setGroupe(rs.getString("groupe"));
                    groupe.setDescription(rs.getString("description"));
                    java.sql.Date dateSql = rs.getDate("date_de_creation");
                    LocalDate date = dateSql.toLocalDate();
                    groupe.setDateDeCreation(date);
                    groupe.setNom(rs.getString("nom"));
                    groupe.setImage(rs.getString("image"));
                    searchResults.add(groupe);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error searching for groups: " + ex.getMessage());
        }
        return searchResults;
    }


}
