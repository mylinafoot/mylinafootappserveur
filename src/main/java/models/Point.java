package models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Point extends PanacheEntity {
    public Long idSaison;
    public Long idMatch;
    public Long idEquipe;
    public String equipe;
    public String categorie;
    public int journee;
    public int point;

    public int butMarque;
    public int butEncaisse;

}
