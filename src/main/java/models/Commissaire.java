package models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Commissaire extends PanacheEntity {

    public byte[] photo;
    public boolean asPhoto;
    public String nom;
    public String postnom;
    public String prenom;
    public String telephone;
    public String telephone2;
    public String email;
    public String adresse;
    public String region;
    public String categorie;
    public String mdp;
}
