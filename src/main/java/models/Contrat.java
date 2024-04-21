package models;

import java.time.LocalDate;
import java.util.Date;

public class Contrat {

        int id;
        String nom_client, description;
        int montant;
    private Date date_contrat;






        public void setId(int id) {
            this.id = id;
        }
        public void setNom_client(String nom_client) {
            this.nom_client = nom_client;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public void setMontant(int montant) {
            this.montant = montant;
        }
        public int getId() {
            return id;
        }
        public String getNom_client() {
            return nom_client;
        }
        public String getDescription() {
            return description;
        }
        public int getMontant() {
            return montant;
        }
    public Date getDateDeContrat() {
        return date_contrat;
    }

    public void setDateDeContrat(Date dateDeContrat) {
        this.date_contrat = dateDeContrat;
    }



    public Contrat() {

    }

    public Contrat(int id, String nom_client, String description, int montant, Date dateDeContrat) {
        this.id = id;
        this.nom_client = nom_client;
        this.description = description;
        this.montant = montant;
        this.date_contrat= dateDeContrat;
    }

    public Contrat(String nom_client, String description, int montant, Date dateDeContrat) {
        this.nom_client = nom_client;
        this.description = description;
        this.montant = montant;
        this.date_contrat = dateDeContrat;
    }

    @Override
    public String toString() {
        return "Contrat{" +
                "id=" + id +
                ", nom_client='" + nom_client + '\'' +
                ", description='" + description + '\'' +
                ", montant=" + montant +
                ", dateDeContrat=" + date_contrat+
                '}';
    }


    }



















