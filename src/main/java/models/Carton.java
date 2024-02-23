package models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Carton extends PanacheEntity {
    public Long idMatch;
    public Long idJoueur;
    public String typeCarton;
    public String raison;
}
