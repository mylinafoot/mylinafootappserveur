package models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Butteur extends PanacheEntity {
    public Long idMatch;
    public Long idJoueur;
    public String typeBut;
    public String minute;

}
