package models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Billet extends PanacheEntity {
    public Long idMatch;
    public Long idUser;
    public Long idAgent;
    @Column(columnDefinition = "boolean default false")
    public Boolean checker;

    public String journee;
    public String nomEquipeA;
    public String nomEquipeB;
    public String date;
    public String heure;
    public String stade;
    public String typePlace;
    public String qrCode;
}
