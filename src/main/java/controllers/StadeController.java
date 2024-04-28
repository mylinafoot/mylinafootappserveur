package controllers;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Equipe;
import models.Stade;

import java.util.LinkedList;
import java.util.List;

@Path("stade")
public class StadeController {

    @GET
    @Path("one")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@QueryParam("id") Long id) {
        Stade stade = Stade.findById(id);
        return Response.ok(stade).build();
    }
    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEquipe() {
        List<Stade> stades = Stade.listAll();
        return Response.ok(stades).build();
    }
    //
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveEquipe(Stade stade) {
        stade.persist();
        return Response.ok(stade).build();
    }

    //
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateEquipe(Stade stade) {
        Stade stade1 = Stade.findById(stade.id);
        if(stade1 == null){
            return  Response.status(404).build();
        }

        stade1.region = stade.region;
        stade1.ville = stade.ville;
        stade1.nom = stade.nom;
        stade1.nombrePlacePourtoure = stade.nombrePlacePourtoure;
        stade1.nombrePlaceTribuneLateralle = stade.nombrePlaceTribuneLateralle;
        stade1.nombrePlaceTribuneDhonneur = stade.nombrePlaceTribuneDhonneur;
        stade1.nombrePlaceTribuneCentrale = stade.nombrePlaceTribuneCentrale;
        stade1.vip = stade.vip;
        stade1.capaciteStade = stade.capaciteStade;

        return Response.ok(stade1).build();
    }

    //
    @DELETE
    //@Path("id")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response removeEquipe(@QueryParam("id") Long id) {
        Stade.deleteById(id);
        return Response.ok().build();
    }

    //________________________________________________________________

}
