package controllers;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Point;
import models.Classement;
import java.util.*;

@Path("/classement")
public class ClassementController {

    @GET
    @Path("/classement")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClassement() {
        try {
            List<Point> points = Point.listAll(); // Récupération de tous les points

            // Créer une map pour stocker le classement par saison, catégorie et journée
            Map<String, Map<String, Map<Integer, List<Classement>>>> classementParSaisonCategorieEtJournee = new HashMap<>();

            // Parcourir les points et calculer le classement pour chaque saison, catégorie et journée
            for (Point point : points) {
                String saison = point.saison; // Récupérer la saison du point
                String categorie = point.categorie; // Récupérer la catégorie du point
                int journee = point.journee; // Récupérer la journée du point

                // Vérifier si le classement pour cette saison existe déjà
                classementParSaisonCategorieEtJournee
                        .computeIfAbsent(saison, k -> new HashMap<>())
                        .computeIfAbsent(categorie, k -> new HashMap<>())
                        .computeIfAbsent(journee, k -> new ArrayList<>());

                // Récupérer le classement pour la journée, la catégorie et la saison
                List<Classement> classementJournee = classementParSaisonCategorieEtJournee.get(saison).get(categorie).get(journee);
                Classement classement = findOrCreateClassement(classementJournee, point.equipe);

                // Mettre à jour les statistiques du classement
                classement.matchJoue++;
                classement.matchgagne += point.matchgagne;
                classement.matchperdu += point.matchperdu;
                classement.matchnull += point.matchnull;
                classement.butMarque += point.butMarque;
                classement.butEncaisse += point.butEncaisse;
                classement.differencebut += point.differencebut;
                classement.point += point.point;

                // Mettre à jour les champs supplémentaires
                classement.journee = journee;  // Affectation de la journée correcte
                classement.saison = saison;    // Affectation de la saison correcte
                classement.categorie = categorie; // Affectation de la catégorie correcte
            }

            // Trier le classement pour chaque journée, chaque catégorie et chaque saison, et assigner les `id`
            classementParSaisonCategorieEtJournee.forEach((s, mapCategorie) -> {
                mapCategorie.forEach((c, mapJournee) -> {
                    mapJournee.forEach((j, listClassement) -> {
                        // Trier le classement en fonction des critères
                        listClassement.sort((c1, c2) -> {
                            if (c1.point != c2.point) {
                                return c2.point - c1.point;
                            }
                            if (c1.matchperdu != c2.matchperdu) {
                                return c1.matchperdu - c2.matchperdu;
                            }
                            if (c1.butMarque != c2.butMarque) {
                                return c2.butMarque - c1.butMarque;
                            }
                            return c1.equipe.compareToIgnoreCase(c2.equipe);
                        });

                        // Attribuer les `id` en utilisant un Long pour chaque équipe dans le classement
                        for (int i = 0; i < listClassement.size(); i++) {
                            listClassement.get(i).id = (long) (i + 1); // ID en type Long commençant à 1
                        }
                    });
                });
            });

            // Retourner le classement par saison, catégorie et journée
            return Response.ok(classementParSaisonCategorieEtJournee).build();
        } catch (Exception e) {
            // Gestion des erreurs
            return Response.serverError().entity("Erreur lors de la récupération du classement : " + e.getMessage()).build();
        }
    }

    // Méthode pour trouver ou créer un classement pour une équipe dans une journée, une catégorie et une saison
    private Classement findOrCreateClassement(List<Classement> classementJournee, String equipe) {
        for (Classement classement : classementJournee) {
            if (classement.equipe.equals(equipe)) {
                return classement;
            }
        }
        Classement newClassement = new Classement(equipe);
        classementJournee.add(newClassement);
        return newClassement;
    }

    @GET
    @Path("{saison}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClassementBySaison(@PathParam("saison") String saison) {
        try {
            List<Point> points = Point.listAll(); // Récupération de tous les points

            // Créer une map pour stocker le classement par saison, catégorie et journée
            Map<String, Map<String, Map<Integer, List<Classement>>>> classementParSaisonCategorieEtJournee = new HashMap<>();

            // Parcourir les points et calculer le classement pour chaque saison, catégorie et journée
            for (Point point : points) {
                if (point.saison.equals(saison)) { // Vérifier si le point appartient à la saison demandée
                    String categorie = point.categorie; // Récupérer la catégorie du point
                    int journee = point.journee; // Récupérer la journée du point

                    // Vérifier si le classement pour cette catégorie existe déjà dans la saison
                    classementParSaisonCategorieEtJournee
                            .computeIfAbsent(saison, k -> new HashMap<>())
                            .computeIfAbsent(categorie, k -> new HashMap<>())
                            .computeIfAbsent(journee, k -> new ArrayList<>());

                    // Récupérer le classement pour la journée, la catégorie et la saison
                    List<Classement> classementJournee = classementParSaisonCategorieEtJournee.get(saison).get(categorie).get(journee);
                    Classement classement = findOrCreateClassement(classementJournee, point.equipe);

                    // Mettre à jour les statistiques du classement
                    classement.matchJoue++;
                    classement.matchgagne += point.matchgagne;
                    classement.matchperdu += point.matchperdu;
                    classement.matchnull += point.matchnull;
                    classement.butMarque += point.butMarque;
                    classement.butEncaisse += point.butEncaisse;
                    classement.differencebut += point.differencebut;
                    classement.point += point.point;

                    // Mettre à jour les champs supplémentaires
                    classement.journee = journee;  // Affectation de la journée correcte
                    classement.saison = saison;    // Affectation de la saison correcte
                    classement.categorie = categorie; // Affectation de la catégorie correcte
                }
            }

            // Trier le classement pour chaque journée, chaque catégorie et chaque saison, et assigner les `id`
            classementParSaisonCategorieEtJournee.forEach((s, mapCategorie) -> {
                mapCategorie.forEach((c, mapJournee) -> {
                    mapJournee.forEach((j, listClassement) -> {
                        // Trier le classement en fonction des critères
                        listClassement.sort((c1, c2) -> {
                            if (c1.point != c2.point) {
                                return c2.point - c1.point;
                            }
                            if (c1.matchperdu != c2.matchperdu) {
                                return c1.matchperdu - c2.matchperdu;
                            }
                            if (c1.butMarque != c2.butMarque) {
                                return c2.butMarque - c1.butMarque;
                            }
                            return c1.equipe.compareToIgnoreCase(c2.equipe);
                        });

                        // Attribuer les `id` en utilisant un Long pour chaque équipe dans le classement
                        for (int i = 0; i < listClassement.size(); i++) {
                            listClassement.get(i).id = (long) (i + 1); // ID en type Long commençant à 1
                        }
                    });
                });
            });

            // Retourner le classement par saison, catégorie et journée
            return Response.ok(classementParSaisonCategorieEtJournee).build();
        } catch (Exception e) {
            // Gestion des erreurs
            return Response.serverError().entity("Erreur lors de la récupération du classement : " + e.getMessage()).build();
        }
    }
}
