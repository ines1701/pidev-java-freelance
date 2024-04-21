package models;

import java.util.Date;

public class Transaction {

    private int id,montant;
    private String methode_paiement;
    private Contrat contrat;
    private Date date_transaction;

    public Date getDate_transaction() {
        return date_transaction;
    }

    public void setDate_transaction(Date date_transaction) {
        this.date_transaction = date_transaction;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMontant() {
        return montant;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }

    public String getMethode_paiement() {
        return methode_paiement;
    }

    public void setMethode_paiement(String methode_paiement) {
        this.methode_paiement = methode_paiement;
    }

    public Contrat getContrat() {
        return contrat;
    }

    public void setContrat(Contrat contrat) {
        this.contrat = contrat;
    }

    public Transaction() {
    }

    // Updated constructor without id (for creating new transactions)
    public Transaction(int montant, String methode_paiement, Contrat contrat, Date date_transaction) {
        this.montant = montant;
        this.methode_paiement = methode_paiement;
        this.contrat = contrat;
        this.date_transaction = date_transaction; // Set the date_transaction
    }
    public Transaction(int id, int montant, String methode_paiement, Contrat contrat, Date date_transaction) {
        this.id = id;
        this.montant = montant;
        this.methode_paiement = methode_paiement;
        this.contrat = contrat;
        this.date_transaction = date_transaction; // Set the date_transaction
    }


    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", montant=" + montant +
                ", methode_paiement='" + methode_paiement + '\'' +
                ", contrat=" + contrat +
                ", date_transaction=" + date_transaction + // Include date_transaction in toString
                '}';
    }
}
