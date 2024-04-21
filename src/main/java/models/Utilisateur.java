package models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Utilisateur extends PanacheEntity {
    public String nomUtilisateur;

    public String telephone;
    public String province;
    public String pays;
    public String mdp;
}
