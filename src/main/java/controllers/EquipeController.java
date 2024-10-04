package controllers;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Arbitre;
import models.Equipe;

import java.util.LinkedList;
import java.util.List;

@Path("equipe")
public class EquipeController {
    //

    @GET
    @Path("one")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@QueryParam("id") Long id) {
        Equipe equipe = Equipe.findById(id);
        equipe.logo = new byte[0];

        return Response.ok(equipe).build();
    }
    @GET
    @Path("All")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEquipe() {
        List<Equipe> equipes = Equipe.listAll();
        List<Equipe> eqs = new LinkedList<>();
        equipes.forEach((equipe)->{
            //
            equipe.logo = new byte[0];
            eqs.add(equipe);
        });
        return Response.ok(eqs).build();
    }

    @GET
    @Path("All/afficher")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEquipeAfficher(@QueryParam("page") @DefaultValue("1") int page,
                                         @QueryParam("pageSize") @DefaultValue("5") int pageSize) {
        try {
            // 1. Fetch paginated data from your data source (e.g., database)
            List<Equipe> equipes = Equipe.findAll().page(page - 1, pageSize).list(); // Pagination

            // 2. Get total count of equipes
            long totalCount = Equipe.count();

            // 3. Clear logos for all equipes in this page to save memory
            List<Equipe> eqs = new LinkedList<>();
            equipes.forEach((equipe) -> {
                equipe.logo = new byte[0];  // Clear logo data
                eqs.add(equipe);
            });

            // 4. Create a response object with pagination metadata
            return Response.ok(eqs)
                    .header("X-Total-Count", totalCount)  // Total number of equipes
                    .header("X-Page", page)               // Current page
                    .header("X-Page-Size", pageSize)      // Page size (number of items per page)
                    .build();

        } catch (Exception e) {
            // Handle any errors that may occur
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erreur lors de la récupération des équipes.").build();
        }
    }

    //
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveEquipe(Equipe equipe) {
        equipe.persist();
        return Response.ok(equipe).build();
    }

    //
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateEquipe(Equipe equipe) {
        Equipe equipe1 = Equipe.findById(equipe.id);
        if(equipe1 == null){
            return  Response.status(404).build();
        }

        equipe1.nom = equipe.nom;
        equipe1.dateCreation = equipe.dateCreation;
        equipe1.division = equipe.division;
        //equipe1.logo = equipe.logo;
        equipe1.province = equipe.province;

        return Response.ok(equipe1).build();
    }

    @PUT
    @Path("photo")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateEquipePhoto(@QueryParam("id") Long id, byte[] photo) {
        Equipe equipe1 = Equipe.findById(id);
        if(equipe1 == null){
            return  Response.status(404).build();
        }
        equipe1.logo = photo;

        return Response.ok(equipe1).build();
    }

    @PUT
    @Path("logo")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateEquipeLogo(Equipe equipe) {
        Equipe equipe1 = Equipe.findById(equipe.id);
        if(equipe1 == null){
            return  Response.status(404).build();
        }

        equipe1.logo = equipe.logo;

        return Response.ok().build();
    }
    //
    @DELETE
    @Path("id")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response removeEquipe(@PathParam("id") Long id) {
        Equipe.deleteById(id);
        return Response.ok().build();
    }
    //

    @GET
    @Path("logo/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Transactional
    public Response LogoEquipe(@PathParam("id") Long id) {
        Equipe equipe =Equipe.findById(id);
        return Response.ok(equipe.logo).build();
    }
    //
    @GET
    @Path("nom/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Transactional
    public Response nomEquipe(@PathParam("id") Long id) {
        Equipe equipe =Equipe.findById(id);
        return Response.ok(equipe.nom).build();
    }
}
