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
import  models.CartonJaune;
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

                    String joueurEqA = jsonNode.get("joueurEqupeA").asText();
                    System.out.println("joueurEqupeA: "+jsonNode.get("joueurEqupeA"));


                    String joueurEqB = jsonNode.get("joueurEqupeB").get("b").asText();
                    System.out.println("joueurEqupeB: "+jsonNode.get("joueurEqupeB"));

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


                    JsonNode expulsionJoueurEqA = jsonNode.get("expulsionsJoueursGeneralA").get(0);
                    // Accéder aux détails du joueur
                    JsonNode joueureqAexpulNode = expulsionJoueurEqA.get("joueur");

                    // Récupération des informations du joueur
                    int joueureqAexpulId = joueureqAexpulNode.get("id").asInt();
                    String nomjoueureqAexpul = joueureqAexpulNode.get("nom").asText();
                    String prenomJoueureqAexpul = joueureqAexpulNode.get("prenom").asText();
                    int dossardJoueureqAexpul = joueureqAexpulNode.get("numero").asInt();
                    String cartonRougeJoueureqAexpul = "Rouge"; // Type de carton
                    String raisonjoueureqAexpul = expulsionJoueurEqA.get("note").asText();
                    System.out.println("Id"+ joueureqAexpulId +"Nom Joueur EqA expulsé : " + nomjoueureqAexpul + "Prenom Joueur EqA expulsé : " + prenomJoueureqAexpul + ", Raison : " + raisonjoueureqAexpul + "dossard :"+ dossardJoueureqAexpul + "carton :" + cartonRougeJoueureqAexpul);

                    Carton cartonjoueurRougeEqA= new Carton();

                    cartonjoueurRougeEqA.saison=rapport.saison;
                    cartonjoueurRougeEqA.categorie=rapport.categorie;
                    cartonjoueurRougeEqA.journe= rapport.journee;
                    cartonjoueurRougeEqA.idJoueurEqA=joueureqAexpulId;
                    cartonjoueurRougeEqA.NomJoueurEqA=nomjoueureqAexpul;
                    cartonjoueurRougeEqA.PrenomJoueurEqA=prenomJoueureqAexpul;
                    cartonjoueurRougeEqA.dossardJoueurEqA=dossardJoueureqAexpul;
                    cartonjoueurRougeEqA.Typecarton=cartonRougeJoueureqAexpul;
                    cartonjoueurRougeEqA.raison=raisonjoueureqAexpul;
                    // Traitez les données comme nécessaire

                    cartonjoueurRougeEqA.persist();

                    JsonNode expulsionJoueurEqB = jsonNode.get("expulsionsJoueursGeneralB").get(0);
                    // Accéder aux détails du joueur
                    JsonNode joueureqBexpulNode = expulsionJoueurEqB.get("joueur");

                    // Récupération des informations du joueur
                    int joueureqBexpulId = joueureqBexpulNode.get("id").asInt();
                    String nomjoueureqBexpul = joueureqBexpulNode.get("nom").asText();
                    String prenomJoueureqBexpul = joueureqBexpulNode.get("prenom").asText();
                    int dossardJoueureqBexpul = joueureqBexpulNode.get("numero").asInt();
                    String cartonRougeJoueureqBexpul = "Rouge"; // Type de carton
                    String raisonjoueureqBexpul = expulsionJoueurEqB.get("note").asText();
                    System.out.println("Id"+ joueureqBexpulId +"Nom Joueur EqB expulsé : " + nomjoueureqBexpul + "Prenom Joueur EqB expulsé : " + prenomJoueureqBexpul + ", Raison : " + raisonjoueureqBexpul + "dossard :"+ dossardJoueureqBexpul + "carton :" + cartonRougeJoueureqBexpul);

                    Carton cartonjoueurRougeEqB= new Carton();

                    cartonjoueurRougeEqB.saison=rapport.saison;
                    cartonjoueurRougeEqB.categorie=rapport.categorie;
                    cartonjoueurRougeEqB.journe= rapport.journee;
                    cartonjoueurRougeEqB.idJoueurEqB=joueureqBexpulId;
                    cartonjoueurRougeEqB.NomJoueurEqB=nomjoueureqBexpul;
                    cartonjoueurRougeEqB.PrenomJoueurEqB=prenomJoueureqBexpul;
                    cartonjoueurRougeEqB.dossardJoueurEqB=dossardJoueureqBexpul;
                    cartonjoueurRougeEqB.Typecarton=cartonRougeJoueureqBexpul;
                    cartonjoueurRougeEqB.raison=raisonjoueureqBexpul;
                    // Traitez les données comme nécessaire
                    cartonjoueurRougeEqB.persist();


                    JsonNode averstJoueurEqA = jsonNode.get("avertissementsJoueursGeneralA").get(0);
                    // Accéder aux détails du joueur
                    JsonNode AvertisseJoueurEqANode = averstJoueurEqA.get("joueur");

                    // Récupération des informations du joueur
                    int joueureqAavertisId = AvertisseJoueurEqANode.get("id").asInt();
                    String nomjoueureqAavertis = AvertisseJoueurEqANode.get("nom").asText();
                    String prenomjoueureqAavertis = AvertisseJoueurEqANode.get("prenom").asText();
                    int dossardjoueureqAavertis = AvertisseJoueurEqANode.get("numero").asInt();
                    String cartonJaunejoueureqAavertis = "Jaune"; // Type de carton
                    String raisonjoueureqAavertis = averstJoueurEqA.get("note").asText();
                    System.out.println("Id :"+ joueureqAavertisId +"Nom Joueur EqA avertis : " + nomjoueureqAavertis + "Prenom Joueur EqA avertis : " + prenomjoueureqAavertis + ", RaisonA avertis: " + raisonjoueureqAavertis + "dossardA avertis:"+ dossardjoueureqAavertis + "cartonA avertis:" + cartonJaunejoueureqAavertis);

                    CartonJaune cartonjoueurJauneEqA= new CartonJaune();

                    cartonjoueurJauneEqA.saison=rapport.saison;
                    cartonjoueurJauneEqA.categorie=rapport.categorie;
                    cartonjoueurJauneEqA.journe= rapport.journee;
                    cartonjoueurJauneEqA.idJoueurEqA=joueureqAavertisId;
                    cartonjoueurJauneEqA.NomJoueurEqA=nomjoueureqAavertis;
                    cartonjoueurJauneEqA.PrenomJoueurEqA=prenomjoueureqAavertis;
                    cartonjoueurJauneEqA.dossardJoueurEqA=dossardjoueureqAavertis;
                    cartonjoueurJauneEqA.Typecarton=cartonJaunejoueureqAavertis;
                    cartonjoueurJauneEqA.raison=raisonjoueureqAavertis;
                    // Traitez les données comme nécessaire
                    cartonjoueurJauneEqA.persist();


                    JsonNode averstJoueurEqB = jsonNode.get("avertissementsJoueursGeneralB").get(0);
                    // Accéder aux détails du joueur
                    JsonNode AvertisseJoueurEqBNode = averstJoueurEqB.get("joueur");

                    // Récupération des informations du joueur
                    int joueureqBavertisId = AvertisseJoueurEqBNode.get("id").asInt();
                    String nomjoueureqBavertis = AvertisseJoueurEqBNode.get("nom").asText();
                    String prenomjoueureqBavertis = AvertisseJoueurEqBNode.get("prenom").asText();
                    int dossardjoueureqBavertis = AvertisseJoueurEqBNode.get("numero").asInt();
                    String cartonJaunejoueureqBavertis = "Jaune"; // Type de carton
                    String raisonjoueureqBavertis = averstJoueurEqB.get("note").asText();

                    CartonJaune cartonjoueurJauneEqB= new CartonJaune();

                    cartonjoueurJauneEqB.saison=rapport.saison;
                    cartonjoueurJauneEqB.categorie=rapport.categorie;
                    cartonjoueurJauneEqB.journe= rapport.journee;
                    cartonjoueurJauneEqB.idJoueurEqB=joueureqBavertisId;
                    cartonjoueurJauneEqB.NomJoueurEqB=nomjoueureqBavertis;
                    cartonjoueurJauneEqB.PrenomJoueurEqB=prenomjoueureqBavertis;
                    cartonjoueurJauneEqB.dossardJoueurEqB=dossardjoueureqBavertis;
                    cartonjoueurJauneEqB.Typecarton=cartonJaunejoueureqBavertis;
                    cartonjoueurJauneEqB.raison=raisonjoueureqBavertis;
                    // Traitez les données comme nécessaire
                    cartonjoueurJauneEqB.persist();



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

}
