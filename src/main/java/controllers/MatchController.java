package controllers;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Equipe;
import models.Match;

import java.util.*;

@Path("match")
public class MatchController {
    //
    @GET
    @Path("all/{idCalendrier}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMatchsDeLaSaison(@PathParam("idCalendrier") Long idCalendrier) {
        List<Match> matches = Match.find("idCalendrier",idCalendrier).list();

        return Response.ok(matches).build();
    }

    @GET
    @Path("allaffiches/{idCalendrier}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAffiches(@PathParam("idCalendrier") Long idCalendrier) {
        List<Match> matches = Match.find("idCalendrier",idCalendrier).list();
        List<Match> matchess = new LinkedList<>();
        matches.forEach(match -> {
            if(match.afficher){
                matchess.add(match);
            }
        });

        return Response.ok(matchess).build();
    }
    //
    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMatchsDeLaSaison2(@PathParam("saison") String saison) {
        List<Match> matches = Match.find("saison",saison).list();

        return Response.ok(matches).build();
    }

    @GET
    @Path("All/journee/{idCalendrier}/{categorie}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllJourneeDeLaSaison(
            @PathParam("idCalendrier") Long idCalendrier,
                                            @PathParam("categorie") String categorie) {

        HashMap params = new HashMap();
        params.put("idCalendrier",idCalendrier);
        params.put("categorie",categorie);

        List<Match> matches = Match.find("idCalendrier =: idCalendrier and categorie =: categorie",params).list();
        Set<Integer> journees = new HashSet<>();
        //
        matches.forEach((e)->{
            //
            journees.add(e.journee);
        });

        return Response.ok(journees).build();
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

        List<Match> matches = Match.find("idCalendrier =: idCalendrier and categorie =: categorie and journee =: journee",params).list();

        matches.forEach((m)->{
            //
            System.out.println("Match: "+m.journee);
        });

        return Response.ok(matches).build();
    }
    //

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveEquipe(Match match) {
        match.persist();
        return Response.ok(match).build();
    }

    //
    @POST
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveEquipeAll(List<HashMap> matchs) {
        try {


            matchs.forEach(match -> {
                /**
                 *
                 "": 1,
                 "": "playoff",
                 "": "${x[9]}",
                 "": "${d[0]}-${d[1]}-${d[2]}",
                 "": "${x[6]}",
                 "": matchController
                 //""
                 "":
                 //"": place.value,
                 "": "${x[10]}",
                 "": "${x[16]}",
                 "": "${x[14]}",
                 "": "${x[12]}",
                 "vip": "${x[18]}",
                 //
                 "": "${x[11]}",
                 "": "${x[17]}",
                 "": "${x[15]}",
                 "": "${x[13]}",
                 "": "${x[19]}"
                 */
                Long idCalendrier = (Long) match.get("idCalendrier");
                Long idEquipeA = (Long) match.get("idEquipeA");
                String nomEquipeA = (String) match.get("nomEquipeA");
                Long idEquipeB = (Long) match.get("idEquipeB");
                String nomEquipeB = (String) match.get("nomEquipeB");
                String stade = (String) match.get("stade");
                String categorie = (String) match.get("categorie");
                int journee = (Integer) match.get("journee");
                String date = (String) match.get("date");
                String heure = (String) match.get("heure");
                Long commissaire = (Long) match.get("commissaire");
                Long arbitreCentrale = (Long) match.get("arbitreCentrale");
                Long arbitreAssitant1 = (Long) match.get("arbitreAssitant1");
                Long arbitreAssitant2 = (Long) match.get("arbitreAssitant2");
                Long arbitreProtocolaire = (Long) match.get("arbitreProtocolaire");
                int nombreDePlaces = (Integer) match.get("nombreDePlaces");
                int nombreDePlacesPourtour = (Integer) match.get("nombreDePlacesPourtour");
                int nombreDePlacesTribuneCentrale = (Integer) match.get("nombreDePlacesTribuneCentrale");
                int nombreDePlacesTribuneHonneur = (Integer) match.get("nombreDePlacesTribuneHonneur");
                int nombreDePlacesTribuneLateralle = (Integer) match.get("nombreDePlacesTribuneLateralle");
                Double prixPourtour = (Double) match.get("prixPourtour");
                Double prixTribuneCentrale = (Double) match.get("prixTribuneCentrale");
                Double prixTribuneHonneur = (Double) match.get("prixTribuneHonneur");
                Double prixTribuneLateralle = (Double) match.get("prixTribuneLateralle");
                Double prixVIP = (Double) match.get("prixVIP");
                System.out.println("Match: " + date);
            });
            return Response.ok(matchs).build();
        }catch (Exception ex){
            return Response.ok(ex).build();
        }
    }
    //
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateEquipe(Match match) {
        Match match1 = Match.findById(match.id);
        if(match1 == null){
            return  Response.status(404).build();
        }

        match1.stade = match.stade;
        match1.terrainNeutre = match.terrainNeutre;
        match1.quiRecoit = match.quiRecoit;
        match1.date = match.date;
        match1.heure = match.heure;
        match1.commissaire = match.commissaire;
        match1.arbitreCentrale = match.arbitreCentrale;
        match1.arbitreAssitant1 = match.arbitreAssitant1;
        match1.arbitreAssitant2 = match.arbitreAssitant2;
        match1.arbitreProtocolaire = match.arbitreProtocolaire;
        match1.nombreDePlaces = match.nombreDePlaces;
        match1.officierRapporteur = match.officierRapporteur;
        //
        match1.mdpCommissaire = match.mdpCommissaire;
        match1.mdpOfficier = match.mdpOfficier;
        match1.mdpArbitreCentrale = match.mdpArbitreCentrale;
        match1.vip = match.vip;
        //
        match1.nombreDePlacesTribuneCentrale = match.nombreDePlacesTribuneCentrale;
        match1.nombreDePlacesPourtour = match.nombreDePlacesPourtour;
        match1.nombreDePlacesTribuneLateralle = match.nombreDePlacesTribuneLateralle;
        match1.nombreDePlacesTribuneHonneur = match.nombreDePlacesTribuneHonneur;
        //
        match1.prixPourtour = match.prixPourtour;
        match1.prixTribuneCentrale = match.prixTribuneCentrale;
        match1.prixTribuneHonneur = match.prixTribuneHonneur;
        match1.prixTribuneLateralle = match.prixTribuneLateralle;
        //
        return Response.ok(match1).build();
        //
    }

    @PUT
    @Path("afficher")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateAffiche(Match match) {
        Match match1 = Match.findById(match.id);
        if(match1 == null){
            return  Response.status(404).build();
        }

        match1.afficher = match.afficher;
        //
        return Response.ok(match1).build();
        //
    }

    //
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response removeEquipe(@QueryParam("id") Long id) {
        System.out.println("Je supprime le truc: "+id);
        Match.deleteById(id);
        return Response.ok().build();
    }
    //
    @DELETE
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response removeEquipeAll() {
        Match.deleteAll();
        return Response.ok().build();
    }
    //

    //
}
