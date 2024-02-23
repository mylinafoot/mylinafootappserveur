package models.rapport;

import java.util.HashMap;

public class Rapport {
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
    public boolean typeRapport;
    public HashMap rapport;
}
