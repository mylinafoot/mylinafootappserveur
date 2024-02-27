package controllers;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Arbitre;
import models.Commissaire;
import models.Match;

import java.util.HashMap;
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
    @GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@QueryParam("telephone") String telephone, @QueryParam("mdp") String mdp
            , @QueryParam("date") String date, @QueryParam("mdpArbitreCentrale") String mdpArbitreCentrale) {

        //
        System.out.println("telephone: "+telephone+" : mdpp: "+mdp);
        //
        HashMap params = new HashMap();
        params.put("telephone",telephone);
        params.put("mdp",mdp);
        //
        Arbitre arbitre = (Arbitre) Arbitre.find("telephone =: telephone and mdp =: mdp",params).firstResult();
        //
        if (arbitre != null) {

            //
            System.out.println("arbitre: " + arbitre.telephone + " : id: " + arbitre.id);
            //
            HashMap params2 = new HashMap();
            params2.put("arbitreCentrale", arbitre.id);
            params2.put("mdpArbitreCentrale",mdpArbitreCentrale);
            params2.put("jouer", false);
            // and date =: date
            List<Match> matchs = Match.find("arbitreCentrale =: arbitreCentrale and mdpArbitreCentrale =: mdpArbitreCentrale and jouer =: jouer", params2).list();
            //
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
                    reponse.put("arbitreCentrale", arbitre.id);
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
    @GET
    @Path("loginofficier")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOneOfficier(@QueryParam("telephone") String telephone, @QueryParam("mdp") String mdp
            , @QueryParam("date") String date, @QueryParam("mdpOfficier") String mdpOfficier) {

        //
        System.out.println("telephone: "+telephone+" : mdpp: "+mdp);
        //
        HashMap params = new HashMap();
        params.put("telephone",telephone);
        params.put("mdp",mdp);
        //
        Arbitre arbitre = (Arbitre) Arbitre.find("telephone =: telephone and mdp =: mdp",params).firstResult();
        //
        if (arbitre != null) {

            //
            System.out.println("arbitre: " + arbitre.telephone + " : id: " + arbitre.id);
            //
            HashMap params2 = new HashMap();
            params2.put("arbitreProtocolaire", arbitre.id);
            params2.put("mdpOfficier",mdpOfficier);
            params2.put("jouer", false);
            // and date =: date
            List<Match> matchs = Match.find("arbitreProtocolaire =: arbitreProtocolaire and mdpOfficier =: mdpOfficier and jouer =: jouer", params2).list();
            //
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
                    reponse.put("arbitreCentrale", arbitre.id);
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
