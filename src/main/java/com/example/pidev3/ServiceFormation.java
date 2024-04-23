package com.example.pidev3;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ServiceFormation implements CRUD<Formation> {
    private Connection cnx;

    public ServiceFormation() {
        cnx = DBConnection.getInstance().getCnx();
    }

    /*@Override
    public void insertOne(Formation formation) throws SQLException {
        String req = "INSERT INTO `formation`(`titre`, `categorie`, `tuteur`, `updated`) VALUES " +
                "('"+formation.getTitre()+"','"+formation.getCategorie()+"','"+formation.getTuteur()+"','"+formation.getUpdated()+"')";
        Statement st = cnx.createStatement();
        st.executeUpdate(req);
        System.out.println("formation.ajoutée !");
    }*/

    public void insertOne(Formation formation) throws SQLException {
        String req = "INSERT INTO `formation`(`titre`, `categorie`, `tuteur`, `updated`) VALUES (?,?,?,?)";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, formation.getTitre());
        ps.setString(2, formation.getCategorie());
        ps.setString(3, formation.getTuteur());
        ps.setString(4, formation.getUpdated());
        ps.executeUpdate();
    }

    @Override
    public void updateOne(Formation formation) throws SQLException {
        String req = "UPDATE `formation` SET `titre`=?, `categorie`=?, `tuteur`=?, `updated`=? WHERE `id`=?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, formation.getTitre());
        ps.setString(2, formation.getCategorie());
        ps.setString(3, formation.getTuteur());
        ps.setString(4, formation.getUpdated());
        ps.setInt(5, formation.getId()); // Utilisation de l'ID pour identifier la formation à mettre à jour

        ps.executeUpdate();
    }

    public void deleteOne(Formation formation) throws SQLException {
        String req = "DELETE FROM `formation` WHERE `id`=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, formation.getId());
        ps.executeUpdate();
    }

    @Override
    public List<Formation> selectAll() throws SQLException {
        List<Formation> formationList = new ArrayList<>();

        String req = "SELECT * FROM `formation`";
        Statement st = cnx.createStatement();

        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Formation p = new Formation();

            p.setId(rs.getInt(("id")));
            p.setTitre(rs.getString((2)));
            p.setCategorie(rs.getString(("categorie")));
            p.setTuteur(rs.getString((4)));
            p.setUpdated(rs.getString((5)));
            formationList.add(p);
        }

        return formationList;
    }


    public Formation getFormationByTitre(String titre) throws SQLException {
        Connection connection = null /* obtenir la connexion à la base de données */;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Formation formation = null;

        try {
            String query = "SELECT * FROM formation WHERE titre = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, titre);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String categorie = resultSet.getString("categorie");
                String tuteur = resultSet.getString("tuteur");
                String updated = resultSet.getString("updated");

                // Création de l'objet Formation avec les données récupérées de la base de données
                formation = new Formation(id, titre, categorie, tuteur, updated);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            // Fermeture des ressources (ResultSet, PreparedStatement, Connection)
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return formation;
    }


    public int getFormationIdByTitre(String titre) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int formationId = -1; // Valeur par défaut si aucun ID n'est trouvé

        try {
            // Obtenir une connexion à la base de données à partir de DBConnection
            conn = DBConnection.getInstance().getCnx();

            // Préparer la requête SQL
            String query = "SELECT id FROM formation WHERE titre = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, titre);

            // Exécuter la requête SQL
            rs = stmt.executeQuery();

            // Récupérer l'ID de la formation
            if (rs.next()) {
                formationId = rs.getInt("id");
            }
        } finally {
            // Fermer les ressources
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            // Ne pas fermer la connexion ici car elle est gérée par DBConnection
        }

        return formationId;
    }


    public boolean existsByTitre(String titre) throws SQLException {
        // Vérifier si un événement avec le même titre existe dans la base de données
        // Vous pouvez utiliser une requête SQL pour effectuer cette vérification
        // Par exemple, vous pouvez utiliser une requête SELECT avec une clause WHERE pour vérifier l'existence de l'événement avec le titre donné
        String query = "SELECT COUNT(*) FROM formation WHERE titre = ?";

        try (PreparedStatement statement = cnx.prepareStatement(query)) { // Utilisation de cnx au lieu de connection
            statement.setString(1, titre);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return count > 0;
        }
    }

    public ObservableList<String> getAllCategories() {
        ObservableList<String> categories = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT categorie FROM formation";
        try (Connection connection = DBConnection.getInstance().getCnx();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                categories.add(resultSet.getString("categorie"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    public List<Formation> getFormationsByCategorie(String categorie) {
        List<Formation> formations = new ArrayList<>();
        String query = "SELECT * FROM formation WHERE categorie = ?";
        try (Connection connection = DBConnection.getInstance().getCnx();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, categorie);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Formation formation = new Formation(
                            resultSet.getInt("id"),
                            resultSet.getString("titre"),
                            resultSet.getString("categorie"),
                            resultSet.getString("tuteur"),
                            resultSet.getString("updated")
                    );
                    formations.add(formation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return formations;
    }
}





