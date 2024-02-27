package controllers;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Match;
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
                                            @QueryParam("typeRapport") int typeRapport) {
        System.out.println("idCalendrier: "+idCalendrier+" -- typeRapport: "+typeRapport);
        HashMap params = new HashMap();
        params.put("idCalendrier",idCalendrier);
        params.put("typeRapport",typeRapport);

        Rapport rapport = (Rapport) Rapport.find("idCalendrier =: idCalendrier and typeRapport =: typeRapport",params).firstResult();
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
    public Response saveEquipe(Rapport rapport) {
        //
        try {
            System.out.println("idMatch: "+rapport.idMatch);
            //Je dois mettre à jour le match avec idMatch que le client envoit.
            Match match = Match.findById(rapport.idMatch);
            if(rapport.typeRapport == 1){
                //
                match.mdpCommissaire = "";
            } else if (rapport.typeRapport == 2) {
                //
                match.mdpArbitreCentrale = "";
            }else{
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
}
