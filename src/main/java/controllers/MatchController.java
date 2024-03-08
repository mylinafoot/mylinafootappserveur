package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
            ObjectMapper objectMapper = new ObjectMapper();
            String mt = objectMapper.writeValueAsString(matchs);
            JsonNode js = objectMapper.readTree(mt);
            Iterator iterator = js.iterator();
            while (iterator.hasNext()){
                Match matchS = new Match();
                JsonNode match = (JsonNode) iterator.next();
                matchS.idCalendrier = (Long) match.get("idCalendrier").asLong();
                matchS.idEquipeA = (Long) match.get("idEquipeA").asLong();
                matchS.nomEquipeA = (String) match.get("nomEquipeA").asText();
                matchS.idEquipeB = (Long) match.get("idEquipeB").asLong();
                matchS.nomEquipeB = (String) match.get("nomEquipeB").asText();
                matchS.stade = (String) match.get("stade").asText();
                matchS.categorie = (String) match.get("categorie").asText();
                matchS.journee = (Integer) match.get("journee").asInt();
                matchS.date = (String) match.get("date").asText();
                matchS.heure = (String) match.get("heure").asText();
                matchS.commissaire = (Long) match.get("commissaire").asLong();
                matchS.arbitreCentrale = (Long) match.get("arbitreCentrale").asLong();
                matchS.arbitreAssitant1 = (Long) match.get("arbitreAssitant1").asLong();
                matchS.arbitreAssitant2 = (Long) match.get("arbitreAssitant2").asLong();
                matchS.arbitreProtocolaire = (Long) match.get("arbitreProtocolaire").asLong();
                matchS.nombreDePlacesVIP = (Integer) match.get("vip").asInt();
                matchS.nombreDePlacesPourtour = (Integer) match.get("nombreDePlacesPourtour").asInt();
                matchS.nombreDePlacesTribuneCentrale = (Integer) match.get("nombreDePlacesTribuneCentrale").asInt();
                matchS.nombreDePlacesTribuneHonneur = (Integer) match.get("nombreDePlacesTribuneHonneur").asInt();
                matchS.nombreDePlacesTribuneLateralle = (Integer) match.get("nombreDePlacesTribuneLateralle").asInt();
                matchS.prixPourtour = (Double) match.get("prixPourtour").asDouble();
                matchS.prixTribuneCentrale = (Double) match.get("prixTribuneCentrale").asDouble();
                matchS.prixTribuneHonneur = (Double) match.get("prixTribuneHonneur").asDouble();
                matchS.prixTribuneLateralle = (Double) match.get("prixTribuneLateralle").asDouble();
                matchS.prixVIP = (Double) match.get("prixVIP").asDouble();
                //System.out.println("Match: " + date);
            }
            /*
            matchs.forEach(match -> {

            });
            */
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
