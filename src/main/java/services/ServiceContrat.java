package services;

import models.Contrat;
import utils.DBconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceContrat implements CRUD<Contrat> {
    private Connection cnx;

    public ServiceContrat() {
        cnx = DBconnection.getInstance().getCnx();
    }

    @Override
    public void insertOne(Contrat contrat) throws SQLException {
        String req = "INSERT INTO `contrat`(`nom_client`, `montant`, `description`, `date_contrat`,`image`) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, contrat.getNom_client());
        ps.setInt(2, contrat.getMontant());
        ps.setString(3, contrat.getDescription());
        // Assuming dateDeContrat is java.util.Date, and your DB is expecting a SQL date
        ps.setDate(4, contrat.getDateDeContrat() != null ? new java.sql.Date(contrat.getDateDeContrat().getTime()) : null);
        ps.setString(5, contrat.getImage());
        ps.executeUpdate();
    }

    @Override
    public void updateOne(Contrat contrat) throws SQLException {
        String sql = "UPDATE contrat SET nom_client=?, montant=?, description=?,date_contrat=?,image=? WHERE id=?";

        try (PreparedStatement statement = cnx.prepareStatement(sql)) {
            statement.setString(1, contrat.getNom_client());
            statement.setInt(2, contrat.getMontant());
            statement.setString(3, contrat.getDescription());
            statement.setDate(4, contrat.getDateDeContrat() != null ? new java.sql.Date(contrat.getDateDeContrat().getTime()) : null);
            statement.setString(5, contrat.getDescription());
            statement.setInt(6, contrat.getId());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                System.out.println("Contrat not found, update failed.");
            } else {
                System.out.println("Contrat updated successfully.");
            }
        }
    }

    @Override
    public void deleteOne(Contrat contrat) throws SQLException {
        String sql = "DELETE FROM contrat WHERE id=?";

        try (PreparedStatement statement = cnx.prepareStatement(sql)) {
            statement.setInt(1, contrat.getId());

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                System.out.println("Contrat not found, delete failed.");
            } else {
                System.out.println("Contrat deleted successfully.");
            }
        }
    }

    @Override
    public List<Contrat> selectAll() throws SQLException {
        List<Contrat> contratList = new ArrayList<>();

        String req = "SELECT * FROM `contrat`";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Contrat c = new Contrat();
            c.setId(rs.getInt("id"));
            c.setNom_client(rs.getString("nom_client"));
            c.setMontant(rs.getInt("montant"));
            c.setDescription(rs.getString("description"));
            // Handling the SQL date to java.util.Date conversion
            Date date = rs.getDate("date_contrat");
            c.setDateDeContrat(date != null ? new Date(date.getTime()) : null);

            contratList.add(c);
        }

        return contratList;
    }

    public Contrat getByDescription(String description) {
        Contrat contrat = null;
        String query = "SELECT * FROM contrat WHERE description = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, description);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                contrat = new Contrat();
                contrat.setId(rs.getInt("id"));
                contrat.setNom_client(rs.getString("nom_client"));
                contrat.setMontant(rs.getInt("montant"));
                contrat.setDescription(rs.getString("description"));
                Date date = rs.getDate("date_contrat");
                contrat.setDateDeContrat(date != null ? new Date(date.getTime()) : null);
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return contrat;
    }
}
