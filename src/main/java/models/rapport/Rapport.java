package models.rapport;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import java.util.HashMap;

@Entity
public class Rapport extends PanacheEntity {
    public Long idMatch;
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
    public int nombreDePlaces;
    public boolean jouer;
    public int typeRapport;
    public HashMap rapport;
}
