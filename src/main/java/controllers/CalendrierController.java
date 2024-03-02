package controllers;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Calendrier;
import models.Equipe;

import java.util.LinkedList;
import java.util.List;

@Path("calendrier")
public class CalendrierController {
    //
    @GET
    @Path("All")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEquipe() {
        List<Calendrier> calendriers = Calendrier.listAll();

        return Response.ok(calendriers).build();
    }

    @GET
    @Path("actuel")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActuel() {
        List<Calendrier> calendriers = Calendrier.listAll();
        var c = calendriers.get(0);
        return Response.ok(c.id).build();
    }
    //
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveEquipe(Calendrier calendrier) {
        calendrier.persist();
        return Response.ok(calendrier).build();
    }
    //
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateEquipe(Calendrier calendrier) {
        Calendrier calendrier1 = Calendrier.findById(calendrier.id);
        if(calendrier1 == null){
            return  Response.status(404).build();
        }

        calendrier1.saison = calendrier.saison;
        calendrier1.label = calendrier.label;

        return Response.ok(calendrier1).build();
    }

    //
    @DELETE
    @Path("id")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response removeEquipe(@PathParam("id") Long id) {
        Calendrier.deleteById(id);
        return Response.ok().build();
    }
    //

    //
}
