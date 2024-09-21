package models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Classement extends PanacheEntity {
    public String equipe;
    public int matchJoue;
    public int matchgagne;
    public int matchperdu;
    public int matchnull;
    public int butMarque;
    public int butEncaisse;
    public int differencebut;
    public int point;
    public int journee;
    public String saison;
    public String categorie;

    // Default constructor
    public Classement() {
        // Initialize fields to default values
        this.equipe = "";
        this.matchJoue = 0;
        this.matchgagne = 0;
        this.matchperdu = 0;
        this.matchnull = 0;
        this.butMarque = 0;
        this.butEncaisse = 0;
        this.differencebut = 0;
        this.point = 0;
        this.journee = 0;
        this.saison = "";
        this.categorie = "";
    }

    // Parameterized constructor
    public Classement(String equipe) {
        this.equipe = equipe;
        this.matchJoue = 0;
        this.matchgagne = 0;
        this.matchperdu = 0;
        this.matchnull = 0;
        this.butMarque = 0;
        this.butEncaisse = 0;
        this.differencebut = 0;
        this.point = 0;
        this.journee = 0;
        this.saison = "";
        this.categorie = "";
    }
}