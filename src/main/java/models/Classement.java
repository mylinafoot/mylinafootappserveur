package models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Classement extends PanacheEntity {
    public Long idEquipe;
    public String saison;
    public String categorie;
    public int journee;
    public String date;
    public int p;
    public int m;
    public int gd;

}
