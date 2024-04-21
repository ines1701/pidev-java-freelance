package com.example.pidev3;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ServiceRessource implements CRUD<Ressource>{
    private Connection cnx ;

    public ServiceRessource() {
        cnx = DBConnection.getInstance().getCnx();
    }

   @Override
    public void insertOne(Ressource ressource) throws SQLException {
        String req = "INSERT INTO `ressource`(`titre`, `description`, `file`,`formation_id`) VALUES (?,?,?,?)";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, ressource.getTitre());
        ps.setString(2, ressource.getDescription());
        ps.setString(3, ressource.getFile());
        ps.setInt(4,ressource.getFormation_id());

        ps.executeUpdate();

    }

    @Override
    public void updateOne(Ressource ressource) throws SQLException {
        String req = "UPDATE `ressource` SET `titre`=?, `description`=?, `file`=?, `formation_id`=? WHERE `id`=?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, ressource.getTitre());
        ps.setString(2, ressource.getDescription());
        ps.setString(3, ressource.getFile());
        ps.setInt(4,ressource.getFormation_id());
        ps.setInt(5, ressource.getId()); // Utilisation de l'ID pour identifier la ressource à mettre à jour

        ps.executeUpdate();
    }



    @Override
    public void deleteOne(Ressource ressource) throws SQLException {
        String req = "DELETE FROM `ressource` WHERE `id`=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, ressource.getId());
        ps.executeUpdate();

    }

    @Override
    public List<Ressource> selectAll() throws SQLException {
        List<Ressource> ressourceList = new ArrayList<>();

        String req = "SELECT * FROM `ressource`";
        Statement st = cnx.createStatement();

        ResultSet rs = st.executeQuery(req);

        while (rs.next()){
            Ressource p = new Ressource();

            p.setId(rs.getInt(("id")));
            p.setTitre(rs.getString((2)));
            p.setDescription(rs.getString(("description")));
            p.setFile(rs.getString((4)));

            ressourceList.add(p);
        }

        return ressourceList;
    }
    public List<Ressource> getRessourcesForFormation(int formation_id) throws SQLException {
        List<Ressource> ressources = new ArrayList<>();

        // Requête SQL pour sélectionner les ressources associées à la formation
        String query = "SELECT * FROM Ressource WHERE formation_id = ?";

        // Préparer la déclaration SQL
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, formation_id);

            // Exécuter la requête
            try (ResultSet resultSet = statement.executeQuery()) {
                // Parcourir les résultats et créer des objets Ressource
                while (resultSet.next()) {
                    Ressource ressource = new Ressource();
                    ressource.setId(resultSet.getInt("id"));
                    ressource.setTitre(resultSet.getString("titre"));
                    ressource.setDescription(resultSet.getString("description"));
                    ressource.setFile(resultSet.getString("file"));

                    // Ajouter la ressource à la liste
                    ressources.add(ressource);
                }
            }
        }

        return ressources;
    }


}