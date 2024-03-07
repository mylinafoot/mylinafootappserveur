package controllers;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Path("commissaire")
public class CommissaireController {
    //
    @GET
    @Path("one")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@QueryParam("id") Long id) {
        Commissaire commissaire = Commissaire.findById(id);
        commissaire.photo = new byte[0];

        return Response.ok(commissaire).build();
    }
    //
    @GET
    @Path("All")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEquipe() {
        List<Commissaire> commissaires = Commissaire.listAll();
        List<Commissaire> commissaires1 = new LinkedList<>();

        commissaires.forEach((equipe)->{
            //
            equipe.photo = new byte[0];
            commissaires1.add(equipe);
        });

        return Response.ok(commissaires1).build();
    }
    //
    @GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@QueryParam("mdp") String mdp) {

        //
        //
        HashMap params = new HashMap();
        params.put("mdpCommissaire",mdp);
        //

            // and date =: date
            List<Match> matchs = Match.find("mdpCommissaire =: mdpCommissaire",params).list();
            //
        if(!matchs.isEmpty() && matchs != null){
            List<HashMap<String, Object>> responses = new LinkedList<>();
            matchs.forEach((match) -> {
                HashMap<String, Object> reponse = new HashMap();
                if (match != null) {
                    //
                    reponse.put("commissaire", match.commissaire);
                    reponse.put("match", match.id);
                    reponse.put("idCalendrier", match.idCalendrier);
                    reponse.put("idEquipeA", match.idEquipeA);
                    reponse.put("nomEquipeA", match.nomEquipeA);
                    reponse.put("idEquipeB", match.idEquipeB);
                    reponse.put("nomEquipeB", match.nomEquipeB);
                    reponse.put("stade", match.stade);
                    reponse.put("terrainNeutre", match.terrainNeutre);
                    reponse.put("quiRecoit", match.quiRecoit);
                    reponse.put("saison", match.saison);
                    reponse.put("categorie", match.categorie);
                    reponse.put("journee", match.journee);
                    reponse.put("date", match.date);
                    reponse.put("heure", match.heure);
                    reponse.put("arbitreCentrale", match.arbitreCentrale);
                    reponse.put("arbitreAssitant1", match.arbitreAssitant1);
                    reponse.put("arbitreAssitant2", match.arbitreAssitant2);
                    reponse.put("arbitreProtocolaire", match.arbitreProtocolaire);
                    reponse.put("officierRapporteur", match.officierRapporteur);
                    reponse.put("nombreDePlaces", match.nombreDePlaces);
                    reponse.put("typeRapport", 1);
                    reponse.put("jouer", match.jouer);
                    //
                    responses.add(reponse);
                }
            });
            return Response.ok(responses).build();
        }else{
            return Response.status(404).build();
        }


    }
    //
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveEquipe(Commissaire commissaire) {
        commissaire.persist();
        return Response.ok(commissaire).build();
    }
    //
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateEquipe(Commissaire commissaire) {
        Commissaire commissaire1 = Commissaire.findById(commissaire.id);
        if(commissaire1 == null){
            return  Response.status(404).build();
        }

        commissaire1.nom = commissaire.nom;
        commissaire1.postnom = commissaire.postnom;
        commissaire1.prenom = commissaire.prenom;
        //commissaire1.photo = commissaire.photo;
        //commissaire1.asPhoto = commissaire.asPhoto;
        commissaire1.telephone = commissaire.telephone;
        commissaire1.telephone2 = commissaire.telephone2;
        commissaire1.email = commissaire.email;
        commissaire1.adresse = commissaire.adresse;
        commissaire1.region = commissaire.region;
        commissaire1.categorie = commissaire.categorie;
        commissaire1.mdp = commissaire.mdp;

        return Response.ok().build();
    }

    @PUT
    @Path("photo")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateJoueurPhoto(Commissaire commissaire) {
        Commissaire commissaire1 = Commissaire.findById(commissaire.id);
        if(commissaire1 == null){
            return  Response.status(404).build();
        }

        commissaire1.photo = commissaire.photo;

        return Response.ok().build();
    }
    //
    @DELETE
    @Path("id")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response removeEquipe(@PathParam("id") Long id) {
        Commissaire.deleteById(id);
        return Response.ok().build();
    }
    //

    //
    @DELETE
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteCommissaire() {
        //
        Commissaire.deleteAll();
        return Response.ok().build();
    }

    @GET
    @Path("photo/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Transactional
    public Response LogoEquipe(@PathParam("id") Long id) {
        Commissaire commissaire = Commissaire.findById(id);
        return Response.ok(commissaire.photo).build();
    }
    //
}
