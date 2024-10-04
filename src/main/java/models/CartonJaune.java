package models;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class CartonJaune extends PanacheEntity {
    public String saison;
    public String categorie;
    public int journe;
    public int idJoueurEqA;
    public String NomJoueurEqA;
    public String PrenomJoueurEqA;
    public int dossardJoueurEqA;

    public int idJoueurEqB;
    public String NomJoueurEqB;
    public String PrenomJoueurEqB;
    public int dossardJoueurEqB;
    public String Typecarton;
    public String raison;
}
