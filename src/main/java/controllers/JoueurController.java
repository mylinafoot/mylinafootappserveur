package controllers;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Equipe;
import models.Joueur;

import java.util.LinkedList;
import java.util.List;

@Path("joueur")
public class JoueurController {
    //
    @GET
    @Path("All/{idEquipe}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEquipe(@PathParam("idEquipe") Long idEquipe ) {
        List<Joueur> joueurs = Joueur.find("idEquipe",idEquipe).list();
        List<Joueur> jrs = new LinkedList<>();
        joueurs.forEach((joueur)->{
            //
            joueur.photo = new byte[0];
            //
            jrs.add(joueur);
        });
        return Response.ok(jrs).build();
    }
    //
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveEquipe(Joueur joueur) {
        joueur.persist();
        return Response.ok(joueur).build();
    }

    @POST
    @Path("saveall")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveEquipe(List<Joueur> joueurs) {
        joueurs.forEach((joueur -> joueur.persist()));
        //equipe.persist();
        return Response.ok("ok").build();
    }
    //
    //
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateEquipe(Joueur joueur) {
        Joueur joueur1 = Joueur.findById(joueur.id);
        if(joueur1 == null){
            return  Response.status(404).build();
        }

        joueur1.nom = joueur.nom;
        joueur1.postnom = joueur.postnom;
        joueur1.prenom = joueur.prenom;
        //joueur1//.email = joueur.email;
        //joueur1.telephone = joueur.telephone;
        //joueur1.adresse = joueur.adresse;
        joueur1.idEquipe = joueur.idEquipe;
        joueur1.dateNaissance = joueur.dateNaissance;
        joueur1.licence = joueur.licence;
        joueur1.numero = joueur.numero;

        return Response.ok().build();
    }

    @PUT
    @Path("photo")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateJoueurPhoto(Joueur joueur) {
        Joueur joueur1 = Joueur.findById(joueur.id);
        if(joueur1 == null){
            return  Response.status(404).build();
        }

        joueur1.photo = joueur.photo;

        return Response.ok().build();
    }
    //
    @DELETE
    @Path("id")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response removeEquipe(@PathParam("id") Long id) {
        Joueur.deleteById(id);
        return Response.ok().build();
    }

    //
    @DELETE
    @Path("joueurequipe")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response removeEquipeByEqId(@QueryParam("id") Long idEquipe) {
        Joueur.delete("idEquipe",idEquipe);
        return Response.ok().build();
    }

    //
    @GET
    @Path("profile/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Transactional
    public Response LogoEquipe(@PathParam("id") Long id) {
        Joueur joueur =Joueur.findById(id);
        return Response.ok(joueur.photo).build();
    }
}
