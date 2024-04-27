package services;

import models.Contrat;
import models.Transaction;
import utils.DBconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceTransaction implements CRUD<Transaction> {

    private Connection cnx;

    public ServiceTransaction() {
        cnx = DBconnection.getInstance().getCnx();
    }

    @Override
    public void insertOne(Transaction transaction) throws SQLException {
        String req = "INSERT INTO `transaction`(`montant`, `methode_paiement`, `contrat_id`, `date_transaction`) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, transaction.getMontant());
            ps.setString(2, transaction.getMethode_paiement());
            ps.setInt(3, transaction.getContrat().getId());
            ps.setDate(4, new java.sql.Date(transaction.getDate_transaction().getTime())); // Convert java.util.Date to java.sql.Date

            ps.executeUpdate();
        }
    }

    @Override
    public void updateOne(Transaction transaction) throws SQLException {
        String sql = "UPDATE transaction SET montant=?, methode_paiement=?, contrat_id=?, date_transaction=? WHERE id=?";

        try (PreparedStatement statement = cnx.prepareStatement(sql)) {
            statement.setInt(1, transaction.getMontant());
            statement.setString(2, transaction.getMethode_paiement());
            statement.setInt(3, transaction.getContrat().getId());
            statement.setDate(4, new java.sql.Date(transaction.getDate_transaction().getTime())); // Convert java.util.Date to java.sql.Date
            statement.setInt(5, transaction.getId());

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated == 0) {
                System.out.println("Transaction not found, update failed.");
            } else {
                System.out.println("Transaction updated successfully.");
            }
        }
    }

    @Override
    public void deleteOne(Transaction transaction) throws SQLException {
        String sql = "DELETE FROM transaction WHERE id=?";

        try (PreparedStatement statement = cnx.prepareStatement(sql)) {
            statement.setInt(1, transaction.getId());

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                System.out.println("Transaction not found, delete failed.");
            } else {
                System.out.println("Transaction deleted successfully.");
            }
        }
    }

    @Override // Remove @Override annotation
    public List<Transaction> selectAll() throws SQLException {
        List<Transaction> transactionList = new ArrayList<>();

        String req = "SELECT v.id AS id_transaction, v.montant, v.methode_paiement, v.contrat_id, v.date_transaction, u.nom_client, u.description , u.date_contrat " + // Notice the space at the end
                "FROM `transaction` AS v " + // Ensure there's a space before FROM
                "JOIN `contrat` AS u ON v.contrat_id = u.id"; // This is fine as it is

        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(req)) { // Access cnx directly
            while (rs.next()) {
                Transaction t = new Transaction();

                t.setId(rs.getInt("id_transaction"));
                t.setMontant(rs.getInt("montant"));
                t.setMethode_paiement(rs.getString("methode_paiement"));
                t.setDate_transaction(rs.getDate("date_transaction")); // Get java.sql.Date and set it directly

                Contrat contrat = new Contrat();
                contrat.setId(rs.getInt("contrat_id"));
                contrat.setNom_client(rs.getString("nom_client"));
                contrat.setDescription(rs.getString("description"));
                // Handling the SQL date to java.util.Date conversion
                Date date = rs.getDate("date_contrat");
                contrat.setDateDeContrat(date != null ? new Date(date.getTime()) : null);
                t.setContrat(contrat);

                transactionList.add(t);
            }
        }

        return transactionList;
    }

}