package controllers;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Point;
import java.util.*;
import models.Classement;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/classement")

public class ClassementController {

    @GET
    @Path("classement")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClassement() {
        try {
            List<Point> points = Point.listAll(); // Récupération de tous les points

            // Créer une map pour stocker le classement par saison, catégorie et journée
            Map<String, Map<String, Map<Integer, List<Classement>>>> classementParSaisonCategorieEtJournee = new HashMap<>();

            // Parcourir les points et calculer le classement pour chaque saison, catégorie et journée
            for (Point point : points) {
                String saison = point.saison; // Récupérer l'idSaison du point
                String categorie = point.categorie; // Récupérer la catégorie du point
                int journee = point.journee; // Récupérer la journée du point

                // Vérifier si le classement pour cette saison existe déjà
                if (!classementParSaisonCategorieEtJournee.containsKey(saison)) {
                    classementParSaisonCategorieEtJournee.put(saison, new HashMap<>());
                }

                // Vérifier si le classement pour cette catégorie existe déjà dans la saison
                Map<String, Map<Integer, List<Classement>>> classementParCategorieEtJournee = classementParSaisonCategorieEtJournee.get(saison);
                if (!classementParCategorieEtJournee.containsKey(categorie)) {
                    classementParCategorieEtJournee.put(categorie, new HashMap<>());
                }

                // Vérifier si le classement pour cette journée existe déjà dans la catégorie et la saison
                Map<Integer, List<Classement>> classementParJournee = classementParCategorieEtJournee.get(categorie);
                if (!classementParJournee.containsKey(journee)) {
                    classementParJournee.put(journee, new ArrayList<>());
                }

                // Trouver ou créer le classement pour l'équipe dans la journée, la catégorie et la saison
                List<Classement> classementJournee = classementParJournee.get(journee);
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
            }

            // Trier le classement pour chaque journée, chaque catégorie et chaque saison
            for (Map<String, Map<Integer, List<Classement>>> classementParCategorieEtJournee : classementParSaisonCategorieEtJournee.values()) {
                for (Map<Integer, List<Classement>> classementParJournee : classementParCategorieEtJournee.values()) {
                    for (List<Classement> classementJournee : classementParJournee.values()) {
                        Collections.sort(classementJournee, new Comparator<Classement>() {
                            @Override
                            public int compare(Classement c1, Classement c2) {
                                // 1. Plus de point
                                if (c1.point != c2.point) {
                                    return c2.point - c1.point;
                                }
                                // 2. Moins de Match perdu
                                if (c1.matchperdu != c2.matchperdu) {
                                    return c1.matchperdu - c2.matchperdu;
                                }
                                // 3. Plus de but marqués
                                if (c1.butMarque != c2.butMarque) {
                                    return c2.butMarque - c1.butMarque;
                                }
                                // 4. Plus de victoire en confrotation direct
                                // (à implémenter si nécessaire)
                                // 5. Les lettres alphabétiques en fonction des noms des équipes
                                return c1.equipe.compareToIgnoreCase(c2.equipe);
                            }
                        });
                    }
                }
            }

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
                    if (!classementParSaisonCategorieEtJournee.containsKey(saison)) {
                        classementParSaisonCategorieEtJournee.put(saison, new HashMap<>());
                    }

                    Map<String, Map<Integer, List<Classement>>> classementParCategorieEtJournee = classementParSaisonCategorieEtJournee.get(saison);
                    if (!classementParCategorieEtJournee.containsKey(categorie)) {
                        classementParCategorieEtJournee.put(categorie, new HashMap<>());
                    }

                    // Vérifier si le classement pour cette journée existe déjà dans la catégorie et la saison
                    Map<Integer, List<Classement>> classementParJournee = classementParCategorieEtJournee.get(categorie);
                    if (!classementParJournee.containsKey(journee)) {
                        classementParJournee.put(journee, new ArrayList<>());
                    }

                    // Trouver ou créer le classement pour l'équipe dans la journée, la catégorie et la saison
                    List<Classement> classementJournee = classementParJournee.get(journee);
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
                }
            }

            // Trier le classement pour chaque journée, chaque catégorie et chaque saison
            for (Map<String, Map<Integer, List<Classement>>> classementParCategorieEtJournee : classementParSaisonCategorieEtJournee.values()) {
                for (Map<Integer, List<Classement>> classementParJournee : classementParCategorieEtJournee.values()) {
                    for (List<Classement> classementJournee : classementParJournee.values()) {
                        Collections.sort(classementJournee, new Comparator<Classement>() {
                            @Override
                            public int compare(Classement c1, Classement c2) {
                                // 1. Plus de point
                                if (c1.point != c2.point) {
                                    return c2.point - c1.point;
                                }
                                // 2. Moins de Match perdu
                                if (c1.matchperdu != c2.matchperdu) {
                                    return c1.matchperdu - c2.matchperdu;
                                }
                                // 3. Plus de but marqués
                                if (c1.butMarque != c2.butMarque) {
                                    return c2.butMarque - c1.butMarque;
                                }
                                // 4. Plus de victoire en confrotation direct
                                // (à implémenter si nécessaire)
                                // 5. Les lettres alphabétiques en fonction des noms des équipes
                                return c1.equipe.compareToIgnoreCase(c2.equipe);
                            }
                        });
                    }
                }
            }

            // Retourner le classement par saison, catégorie et journée
            return Response.ok(classementParSaisonCategorieEtJournee).build();
        } catch (Exception e) {
            // Gestion des erreurs
            return Response.serverError().entity("Erreur lors de la récupération du classement : " + e.getMessage()).build();
        }
    }

    // Méthode pour trouver ou créer un classement pour une équipe dans une journée, une catégorie et une saison
    private Classement findOrCreateClassementt(List<Classement> classementJournee, String equipe) {
        for (Classement classement : classementJournee) {
            if (classement.equipe.equals(equipe)) {
                return classement;
            }
        }
        Classement newClassement = new Classement(equipe);
        classementJournee.add(newClassement);
        return newClassement;
    }
}