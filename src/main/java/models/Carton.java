package models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Carton extends PanacheEntity {
    public Long idMatch;
    public String typecarton;
    public String raison;
}
