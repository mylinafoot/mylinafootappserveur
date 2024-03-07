package models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Billet extends PanacheEntity {
    public Long idMatch;

    public String journee;
    public String nomEquipeA;
    public String nomEquipeB;
    public String date;
    public String heure;
    public String stade;
    public String typePlace;
    public String qrCode;
}
