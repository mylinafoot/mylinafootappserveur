package models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Joueur extends PanacheEntity {

    public Long idEquipe;
    public byte[] photo;
    public boolean asPhoto;
    public String nom;
    public String postnom;
    public String prenom;
    public String dateNaissance;
    public String licence;
    public String numero;
}
