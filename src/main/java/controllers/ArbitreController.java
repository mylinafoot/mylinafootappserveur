package controllers;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Arbitre;
import models.Commissaire;

import java.util.LinkedList;
import java.util.List;

@Path("arbitre")
public class ArbitreController {
    //
    @GET
    @Path("one")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@QueryParam("id") Long id) {
        Arbitre arbitre = Arbitre.findById(id);
        arbitre.photo = new byte[0];

        return Response.ok(arbitre).build();
    }
    //
    @GET
    @Path("All")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEquipe() {
        List<Arbitre> arbitres = Arbitre.listAll();
        List<Arbitre> arbitres1 = new LinkedList<>();

        arbitres.forEach((equipe)->{
            //
            equipe.photo = new byte[0];
            arbitres1.add(equipe);
        });

        return Response.ok(arbitres1).build();
    }
    //
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveEquipe(Arbitre arbitre) {
        arbitre.persist();
        return Response.ok(arbitre).build();
    }
    //
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateEquipe(Arbitre arbitre) {
        Arbitre arbitre1 = Arbitre.findById(arbitre.id);
        if(arbitre1 == null){
            return  Response.status(404).build();
        }

        arbitre1.nom = arbitre.nom;
        arbitre1.postnom = arbitre.postnom;
        arbitre1.prenom = arbitre.prenom;
        //arbitre1.photo = arbitre.photo;
        //arbitre1.asPhoto = arbitre.asPhoto;
        arbitre1.telephone = arbitre.telephone;
        arbitre1.telephone2 = arbitre.telephone2;
        arbitre1.email = arbitre.email;
        arbitre1.adresse = arbitre.adresse;
        arbitre1.region = arbitre.region;
        arbitre1.categorie = arbitre.categorie;
        arbitre1.mdp = arbitre.mdp;

        return Response.ok().build();
    }

    @PUT
    @Path("photo")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateJoueurPhoto(Arbitre arbitre) {
        Arbitre arbitre1 = Arbitre.findById(arbitre.id);
        if(arbitre1 == null){
            return  Response.status(404).build();
        }

        arbitre1.photo = arbitre.photo;

        return Response.ok().build();
    }
    //
    @DELETE
    @Path("id")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response removeEquipe(@PathParam("id") Long id) {
        Arbitre.deleteById(id);
        return Response.ok().build();
    }
    //

    @GET
    @Path("photo/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Transactional
    public Response LogoEquipe(@PathParam("id") Long id) {
        Arbitre arbitre = Arbitre.findById(id);
        return Response.ok(arbitre.photo).build();
    }
    //
}
