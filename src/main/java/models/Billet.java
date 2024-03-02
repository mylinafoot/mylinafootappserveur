package models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Billet extends PanacheEntity {
    public Long idMatch;
    public String typePlace;
    public String qrCode;
}
