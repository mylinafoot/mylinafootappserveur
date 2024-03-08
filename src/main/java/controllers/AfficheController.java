package controllers;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Affiche;
import models.Match;

import java.util.List;

@Path("affiche")
public class AfficheController {
    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMatchsDeLaSaison2() {
        List<Affiche> affiches = Affiche.listAll();

        return Response.ok(affiches).build();
    }

    @GET
    @Path("one")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@QueryParam("idMatch") Long idMatch) {
        Affiche affiche = Affiche.find("idMatch",idMatch).firstResult();
        return Response.ok(affiche).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveEquipe(Affiche affiche) {
        affiche.persist();
        return Response.ok(affiche).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveEquipe(@QueryParam("idMatch") Long idMatch) {
        Affiche.delete("idMatch",idMatch);
        return Response.ok().build();
    }
}
