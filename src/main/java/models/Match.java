package models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Match extends PanacheEntity {

    public Long idCalendrier;
    public Long idEquipeA;
    public String nomEquipeA;
    public Long idEquipeB;
    public String nomEquipeB;
    public String stade;
    public boolean terrainNeutre;
    public Long quiRecoit;
    public String saison;
    public String categorie;
    public int journee;
    public String date;
    public String heure;
    public Long commissaire;
    public Long arbitreCentrale;
    public Long arbitreAssitant1;
    public Long arbitreAssitant2;
    public Long arbitreProtocolaire;
    public Long officierRapporteur;
    public int nombreDePlaces;
    @Column(columnDefinition = "boolean default false")
    public boolean jouer;
}
