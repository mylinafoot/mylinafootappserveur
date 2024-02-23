package models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Calendrier extends PanacheEntity {
    public String saison;
    public String label;

}
