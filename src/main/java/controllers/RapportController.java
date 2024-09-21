package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Classement;
import models.Commissaire;
import models.Match;
import models.Point;
import models.Joueur;
import models.Carton;
import models.rapport.Rapport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Path("rapport")
public class RapportController {
    private static final Logger log = LoggerFactory.getLogger(RapportController.class);

    @GET
    @Path("All/{saison}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMatchsDeLaSaison(@PathParam("saison") String saison) {
        List<Rapport> rapports = Rapport.find("saison",saison).list();

        return Response.ok(rapports).build();
    }
    //

    @GET
    @Path("All/journee/{idCalendrier}/{categorie}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllJourneeDeLaSaison(
            @PathParam("idCalendrier") Long idCalendrier,
            @PathParam("categorie") String categorie) {

        HashMap params = new HashMap();
        params.put("idCalendrier",idCalendrier);
        params.put("categorie",categorie);

        List<Rapport> rapports = Rapport.find("idCalendrier =: idCalendrier and categorie =: categorie",params).list();
        //Set<Integer> journees = new HashSet<>();
        //

        return Response.ok(rapports).build();
    }
    //

    @GET
    @Path("All/match/{idCalendrier}/{categorie}/{journee}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMatchsDeLaJournee(@PathParam("idCalendrier") Long idCalendrier,
                                            @PathParam("categorie") String categorie,
                                            @PathParam("journee") String journee) {

        HashMap params = new HashMap();
        params.put("idCalendrier",idCalendrier);
        params.put("categorie",categorie);
        params.put("journee",journee);

        List<Rapport> rapports = Rapport.find("idCalendrier =: idCalendrier and categorie =: categorie and journee =: journee",params).list();


        return Response.ok(rapports).build();
    }
    //
    @GET
    @Path("rapport")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRapport(@QueryParam("idCalendrier") Long idCalendrier,
                                            @QueryParam("typeRapport") int typeRapport, @QueryParam("idMatch") Long idMatch) {

        System.out.println("idCalendrier: "+idCalendrier+" -- typeRapport: "+typeRapport);
        HashMap params = new HashMap();
        params.put("idCalendrier",idCalendrier);
        params.put("typeRapport",typeRapport);
        params.put("idMatch",idMatch);

        Rapport rapport = (Rapport) Rapport.find("idCalendrier =: idCalendrier and typeRapport =: typeRapport and idMatch =: idMatch",params).firstResult();
        //
        Match match = Match.findById(rapport.idMatch);
        //
        if (typeRapport == 1) {
            //
            if(rapport != null && match.mdpCommissaire.isEmpty()){
                return Response.ok(rapport).build();
            }else{
                return Response.status(404).build();
            }
        }
        if (typeRapport == 2) {
            //
            if(rapport != null && match.mdpArbitreCentrale.isEmpty()){
                return Response.ok(rapport).build();
            }else{
                return Response.status(404).build();
            }
        }
        if (typeRapport == 3) {
            //
            if(rapport != null && match.mdpOfficier.isEmpty()){
                return Response.ok(rapport).build();
            }else{
                return Response.status(404).build();
            }
        }
        //
        return Response.status(404).build();

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveRapport(Rapport rapport) {
        //
        try {
            System.out.println("idMatch: "+rapport.idMatch);
            //Je dois mettre à jour le match avec idMatch que le client envoit.
            Match match = Match.findById(rapport.idMatch);
            if(rapport.typeRapport == 1){
                //
                match.mdpCommissaire = "";
            }
            if (rapport.typeRapport == 2) {
                //
                match.mdpArbitreCentrale = "";
                //
                try {
                    System.out.println("idMatch: "+rapport.idMatch);
                    //Je dois mettre à jour le match avec idMatch que le client envoit.
                    //Match match = Match.findById(rapport.idMatch);
                    //
                    HashMap rapporte = rapport.rapport;
                    //
                    ObjectMapper obj = new ObjectMapper();
                    //
                    String rapText = obj.writeValueAsString(rapporte);
                    //
                    JsonNode jsonNode = obj.readTree(rapText);
                    //
                    int scoreEqA = jsonNode.get("scoreFin").get("a").asInt();

                    System.out.println("Le eqA: "+jsonNode.get("scoreFin").get("a"));
                    System.out.println("Le score eqA: "+jsonNode.get("scoreFin").get("a"));
                    System.out.println("__________________________________________________");

                    String cartonjauneEqA = jsonNode.get("avertissementsJoueursGeneralA").get("a").asText();
                    System.out.println("cartonJauneA: "+jsonNode.get("avertissementsJoueursGeneralA").get("a"));

                    String cartonjauneEqB = jsonNode.get("avertissementsJoueursGeneralB").get("b").asText();
                    System.out.println("cartonJauneB: "+jsonNode.get("avertissementsJoueursGeneralB").get("b"));

                    String cartonrougeEqA = jsonNode.get("expulssionsJoueursGeneralA").get("a").asText();
                    System.out.println("cartonRougeA: "+jsonNode.get("expulssionsJoueursGeneralA").get("a"));

                    String cartonrougeEqB = jsonNode.get("expulssionsJoueursGeneralB").get("b").asText();
                    System.out.println("cartonRougeB: "+jsonNode.get("expulssionsJoueursGeneralB").get("b"));

                    String joueurEqA = jsonNode.get("joueurEqupeA").get("a").asText();
                    System.out.println("joueurEqupeA: "+jsonNode.get("joueurEqupeA").get("a"));


                    String joueurEqB = jsonNode.get("joueurEqupeB").get("b").asText();
                    System.out.println("joueurEqupeB: "+jsonNode.get("joueurEqupeB").get("a"));

                    int scoreEqB = jsonNode.get("scoreFin").get("b").asInt();

                    System.out.println("Le eqB: "+jsonNode.get("scoreFin").get("b"));
                    System.out.println("Le score eqB: "+jsonNode.get("scoreFin").get("b"));
                    //
                    //scoreFin//
                    Point pointEquipeA = new Point();
                    pointEquipeA.equipe = rapport.nomEquipeA;
                    pointEquipeA.idEquipe = rapport.idEquipeA;
                    pointEquipeA.idSaison = rapport.idCalendrier;
                    pointEquipeA.idMatch = rapport.idMatch;
                    pointEquipeA.butMarque = scoreEqA;
                    pointEquipeA.butEncaisse = scoreEqB;
                    pointEquipeA.journee = rapport.journee;
                    pointEquipeA.categorie = rapport.categorie;
                    pointEquipeA.point = scoreEqA > scoreEqB ? 3 : scoreEqA == scoreEqB ? 1 : 0;
                    pointEquipeA.matchnull = scoreEqA == scoreEqB ? 1 : 0;
                    pointEquipeA.matchperdu = scoreEqA < scoreEqB ? 1 : 0;
                    pointEquipeA.matchgagne = scoreEqA > scoreEqB ? 1 : 0;
                    pointEquipeA.differencebut = scoreEqA - scoreEqB;
                    pointEquipeA.persist();
                    //
                    Point pointEquipeB = new Point();
                    pointEquipeB.equipe = rapport.nomEquipeB;
                    pointEquipeB.idEquipe = rapport.idEquipeB;
                    pointEquipeB.idSaison = rapport.idCalendrier;
                    pointEquipeB.idMatch = rapport.idMatch;
                    pointEquipeB.butMarque = scoreEqB;
                    pointEquipeB.butEncaisse = scoreEqA;
                    pointEquipeB.journee = rapport.journee;
                    pointEquipeB.categorie = rapport.categorie;
                    pointEquipeB.point = scoreEqB > scoreEqA ? 3 : scoreEqA == scoreEqB ? 1 : 0;
                    pointEquipeB.matchnull = scoreEqB == scoreEqA ? 1 : 0;
                    pointEquipeA.matchgagne = scoreEqB > scoreEqA ? 1 : 0;
                    pointEquipeA.matchperdu = scoreEqB < scoreEqA ? 1 : 0;
                    pointEquipeA.differencebut = scoreEqB - scoreEqA;
                    pointEquipeB.persist();


                    Carton cartonjoueur= new Carton();
                    cartonjoueur.idjoueur=rapport.idjoueur;
                    cartonjoueur.idMatch=rapport.idMatch;
                    cartonjoueur.raison= rapport.raison;
                    cartonjoueur.typecarton= rapport.typecarton;

                    cartonjoueur.persist();
                    //CARTON
                    /*Carton cartonjoeur = new Carton();
                    //cartonjoeur.idJoueur = rapport.jo;
                    cartonjoeur.idMatch = rapport.idMatch;
                    cartonjoeur.raison = rapport.*/
                    //
                    rapport.persist();
                }catch (Exception ex){
                    //
                    System.out.println("Erreur du à : "+ex);
                }
            }
            if (rapport.typeRapport == 3) {
                //
                match.mdpOfficier = "";
            }
            //
            //match.jouer = true;
            //
            rapport.persist();
        }catch (Exception ex){
            //
            System.out.println("Erreur du à : "+ex);
        }

        return Response.ok().build();
    }


    //

    @POST
    @Path("test")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveRapportTest(Rapport rapport) {
        //
        try {
            System.out.println("idMatch: "+rapport.idMatch);
            //Je dois mettre à jour le match avec idMatch que le client envoit.
            //Match match = Match.findById(rapport.idMatch);
            //
            // Imprimer l'objet rapport pour voir sa structure
            System.out.println("Objet rapport : " + new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(rapport));

            HashMap rapporte = rapport.rapport;
            //
            ObjectMapper obj = new ObjectMapper();
            //
            String rapText = obj.writeValueAsString(rapporte);
            //
            // Imprimer le JsonNode pour voir sa structure
            //System.out.println("JsonNode : " + new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode));
            JsonNode jsonNode = obj.readTree(rapText);
            //
            int scoreEqA = jsonNode.get("scoreFin").get("a").asInt();

            System.out.println("Le eqA: "+jsonNode.get("scoreFin").get("a"));
            System.out.println("Le score eqA: "+jsonNode.get("scoreFin").get("a"));
            System.out.println("Le score eqA: "+jsonNode.get("scoreFin").get("a"));
            System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode));
            System.out.println("__________________________________________________");

            int scoreEqB = jsonNode.get("scoreFin").get("b").asInt();
            System.out.println("Le eqB: "+jsonNode.get("scoreFin").get("b"));
            System.out.println("Le score eqB: "+jsonNode.get("scoreFin").get("b"));
            //

            //L'idée ici est  de reccupérer les valeurs des matchs null. Et pour se faire, nous comparons juste scoreEqA et scoreEqB
            // Et directement on reccupère la valeur de l'une des deux variable

            //scoreFin//
            Point pointEquipeA = new Point();
            pointEquipeA.equipe = rapport.nomEquipeA;
            pointEquipeA.idEquipe = rapport.idEquipeA;
            pointEquipeA.idSaison = rapport.idCalendrier;
            pointEquipeA.saison = rapport.saison;
            pointEquipeA.idMatch = rapport.idMatch;
            pointEquipeA.butMarque = scoreEqA;
            pointEquipeA.butEncaisse = scoreEqB;
            pointEquipeA.point = scoreEqA > scoreEqB ? 3 : scoreEqA == scoreEqB ? 1 : 0;

            pointEquipeA.persist();
            //
            System.out.println("Valeur MATCH NUL EQUIPE A : " + pointEquipeA.point);

            Point pointEquipeB = new Point();
            pointEquipeB.equipe = rapport.nomEquipeB;
            pointEquipeB.idEquipe = rapport.idEquipeB;
            pointEquipeB.idSaison = rapport.idCalendrier;
            pointEquipeB.saison = rapport.saison;
            pointEquipeB.idMatch = rapport.idMatch;
            pointEquipeB.butMarque = scoreEqB;
            pointEquipeB.butEncaisse = scoreEqA;
            pointEquipeB.point = scoreEqB > scoreEqA ? 3 : scoreEqA == scoreEqB ? 1 : 0;

            pointEquipeB.persist();
            //
            rapport.persist();

            System.out.println("Valeur MATCH NUL EQUIPE B : " + pointEquipeB.point);
        }catch (Exception ex){
            //
            System.out.println("Erreur du à : "+ex);
        }

        return Response.ok().build();
    }
    //

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response LogoEquipe(@QueryParam("id") Long id) {
        Rapport.deleteById(id);
        return Response.ok().build();
    }

    //API CLASSEMENT
    /*@GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Point> points = Point.listAll();
        System.out.println("Points récupérés : " + points.size());
        points.forEach(point -> System.out.println("LUCIENNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN" +point)); // Affiche chaque point
        return Response.ok(points).build();
    }

    @GET
    @Path("classement")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getClassement(
            @QueryParam("idCalendrier") Long idCalendrier,
            @QueryParam("categorie") String categorie,
            @QueryParam("journee") int journee,
            @QueryParam("typeCompetition") String typeCompetition) {

        // Paramètres pour filtrer les points
        HashMap<String, Object> params = new HashMap<>();
        params.put("idSaison", idCalendrier);
        params.put("categorie", categorie);

        // Récupération des points associés à la saison et à la catégorie
        List<Point> points = Point.find("idSaison =: idSaison and categorie =: categorie", params).list();

        // Vérification si des points sont trouvés
        if (points == null || points.isEmpty()) {
            System.out.println("Aucun point trouvé pour la saison: " + idCalendrier + " et la catégorie: " + categorie);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Aucun point trouvé pour les critères donnés.")
                    .build();
        }

        // Récupérer les équipes à partir des points trouvés
        Set<Long> equipes = getEquipes(points);

        if (equipes.isEmpty()) {
            System.out.println("Aucune équipe trouvée pour les points fournis.");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Aucune équipe trouvée pour les critères donnés.")
                    .build();
        }

        // Calcul du classement
        List<Classement> classement = calculateClassement(equipes, points, journee, typeCompetition);

        return Response.ok(classement).build();
    }

    // Méthode pour calculer le classement à partir des points
    private List<Classement> calculateClassement(Set<Long> equipes, List<Point> pts, int journee, String typeCompetition) {
        List<Classement> classements = new LinkedList<>();

        for (Long equipeId : equipes) {
            Classement classement = new Classement();
            AtomicInteger mj = new AtomicInteger(0); // Matches Joués
            AtomicInteger mg = new AtomicInteger(0); // Matches Gagnés
            AtomicInteger mp = new AtomicInteger(0); // Matches Perdus
            AtomicInteger mn = new AtomicInteger(0); // Matches Nuls
            AtomicInteger bm = new AtomicInteger(0); // Buts Marqués
            AtomicInteger bc = new AtomicInteger(0); // Buts Encaissés
            AtomicInteger ptsTotal = new AtomicInteger(0); // Points

            // Récupération des données des points
            pts.forEach(point -> {
                if (point.idEquipe.equals(equipeId) && point.journee <= journee) {
                    mj.incrementAndGet();
                    bm.addAndGet(point.butMarque);
                    bc.addAndGet(point.butEncaisse);
                    ptsTotal.addAndGet(point.point);

                    if (point.point == 3) mg.incrementAndGet(); // Victoire
                    if (point.point == 1) mn.incrementAndGet(); // Match Nul
                    if (point.point == 0) mp.incrementAndGet(); // Défaite
                }
            });

            classement.idEquipe = equipeId;
            classement.mj = mj.get(); // Matches Joués
            classement.mg = mg.get(); // Matches Gagnés
            classement.mp = mp.get(); // Matches Perdus
            classement.mn = mn.get(); // Matches Nuls
            classement.bm = bm.get(); // Buts Marqués
            classement.bc = bc.get(); // Buts Encaissés
            classement.diff = bm.get() - bc.get();  // Calcul de la différence de buts
            classement.pts = ptsTotal.get(); // Points Total
            classement.journee = journee; // Journée du match
            classement.typeCompetition = typeCompetition; // Type de compétition

            // Persister chaque classement dans la base de données
            classement.persist();

            // Log pour chaque équipe
            System.out.println("Traitement de l'équipe: " + equipeId);
            System.out.println("Matches Joués: " + mj.get());
            System.out.println("Points Total: " + ptsTotal.get());
        }

        // Tri par points, puis par différence de buts
        classements.sort((c1, c2) -> {
            // Premier tri par points
            int result = Integer.compare(c2.pts, c1.pts);
            if (result == 0) {
                // Si les points sont égaux, trier par différence de buts
                result = Integer.compare(c2.diff, c1.diff);
                if (result == 0) {
                    // Si la différence de buts est aussi égale, trier par nombre de buts marqués
                    result = Integer.compare(c2.bm, c1.bm);
                }
            }
            return result;
        });

        return classements;
    }

    // Méthode pour obtenir les équipes à partir des points
    private Set<Long> getEquipes(List<Point> tabs) {
        Set<Long> equipes = new HashSet<>();
        tabs.forEach((e) -> equipes.add(e.idEquipe));
        return equipes;
    }*/

}
