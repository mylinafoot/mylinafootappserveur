package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Commissaire;
import models.Match;
import models.Point;
import models.rapport.Rapport;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Path("rapport")
public class RapportController {
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
                    pointEquipeB.persist();
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
            pointEquipeA.point = scoreEqA > scoreEqB ? 3 : scoreEqA == scoreEqB ? 1 : 0;
            pointEquipeA.persist();
            //
            Point pointEquipeB = new Point();
            pointEquipeB.equipe = rapport.nomEquipeB;
            pointEquipeB.idEquipe = rapport.idEquipeB;
            pointEquipeB.idSaison = rapport.idCalendrier;
            pointEquipeB.idMatch = rapport.idMatch;
            pointEquipeB.butMarque = scoreEqB;
            pointEquipeB.butEncaisse = scoreEqA;
            pointEquipeB.point = scoreEqB > scoreEqA ? 3 : scoreEqA == scoreEqB ? 1 : 0;
            pointEquipeB.persist();
            //
            rapport.persist();
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
