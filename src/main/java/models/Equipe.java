package models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Equipe extends PanacheEntity {
    public String nom;
    public String province;
    public byte[] logo;
    public String dateCreation;
    public String division;
}
