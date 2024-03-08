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
    public String mdpCommissaire;
    public Long arbitreCentrale;
    public String mdpArbitreCentrale;
    public Long arbitreAssitant1;
    public Long arbitreAssitant2;
    public Long arbitreProtocolaire;
    public Long officierRapporteur;
    public String mdpOfficier;
    public int nombreDePlaces;
    @Column(columnDefinition = "Integer default '0'")
    public int nombreDePlacesPourtour = 0;
    @Column(columnDefinition = "Integer default '0'")
    public int nombreDePlacesTribuneCentrale = 0;
    @Column(columnDefinition = "Integer default '0'")
    public int nombreDePlacesTribuneHonneur = 0;
    @Column(columnDefinition = "Integer default '0'")
    public int nombreDePlacesTribuneLateralle = 0;

    @Column(columnDefinition = "Integer default '0'")
    public int vip;
    @Column(columnDefinition = "boolean default false")
    public boolean jouer;
    //
    @Column(columnDefinition = "DOUBLE PRECISION DEFAULT 0.0")
    public double prixPourtour;
    @Column(columnDefinition = "DOUBLE PRECISION DEFAULT 0.0")
    public double prixTribuneLateralle;
    @Column(columnDefinition = "DOUBLE PRECISION DEFAULT 0.0")
    public double prixTribuneHonneur;
    @Column(columnDefinition = "DOUBLE PRECISION DEFAULT 0.0")
    public double prixTribuneCentrale;

    @Column(columnDefinition = "DOUBLE PRECISION DEFAULT 0.0")
    public double prixVIP;
    @Column(columnDefinition = "Boolean default true")
    public boolean prevente;
    @Column(columnDefinition = "Boolean default true")
    public boolean envente;

    @Column(columnDefinition = "Boolean default false")
    public boolean afficher;

}
