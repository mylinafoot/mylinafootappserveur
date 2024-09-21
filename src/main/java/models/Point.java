package models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Point extends PanacheEntity {
    public Long idSaison;
    public Long idMatch;
    public Long idEquipe;
    public String equipe;
    public String saison;
    public String categorie; // Play off, classique et play down
    public int journee;
    public int point;

    public int butMarque;
    public int butEncaisse;
    public int matchnull;
    public int matchperdu;
    public int matchgagne;
    public int differencebut;

    ///////
   /* public int matchjoue; // Matches Joués
    public int matchgagne; // Matches Gagnés
    public int matchperdu; // Matches Perdus
    public int matchnull; // Matches Nuls
    public int diffbutmarquesetbutencaises; // Différence entre Buts Marqués et Buts Encaissés*/
}
