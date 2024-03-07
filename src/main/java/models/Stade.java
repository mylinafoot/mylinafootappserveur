package models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Stade extends PanacheEntity {
    //
    /**
     * REGION;VILLE;STADE;NBRE DE PLACES GRAND PUBLIC;NBRE DE PLACE TRIBUNES LATERALLES;NBRE DE PLACE TRIBUNE D'HONNEUR;NBRE DE PLACE TRIBUNE CENTRALE;VIP;CAPACITE STADE ;NBRE DES MATCH;;
     * KINSHASA ;KINSHASA;TATA RAPHAEL ;36200;7800;250;750;0;45000;14;;
     * NORD-KIVU ;GOMA ;STADE DE L'UNITE ;10000;0;0;200;0;10200;7;;
     * KINDU ;MANIEMA;JOSEPH KABILA ;22000;800;400;0;0;23200;7;;
     * KATANGA ;L'SHI ;KIBASA MALIBA ;20000;5000;2000;300;200;27300;6;;
     * KATANGA ;L'SHI ;KAMALONDO ;6500;2000;3000;6000;500;17500;20;;
     */

    public String region;
    public String ville;

    public String nom;
    public int nombrePlacePourtoure;
    public int nombrePlaceTribuneLateralle;
    public int nombrePlaceTribuneDhonneur;
    public int nombrePlaceTribuneCentrale;
    public int vip;
    public int capaciteStade;

}
