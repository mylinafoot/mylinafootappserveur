package controllers;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Utilisateur;

import java.util.HashMap;

@Path("utilisateur")
public class UtilisateurController {

    @GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setLoger(@QueryParam("telephone") String telephone, @QueryParam("mdp") String mdp){
        //
        HashMap params = new HashMap();
        params.put("telephone",telephone);
        params.put("mdp",mdp);

        Utilisateur utilisateur = (Utilisateur) Utilisateur.find("telephone =: telephone and mdp =: mdp",params).firstResult();
        if (utilisateur != null) {
            //
            return Response.ok(utilisateur).build();
        }
        return Response.status(404).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response enregistrerUtilisateur(Utilisateur utilisateur){
        //

        Utilisateur utilisateur1 = (Utilisateur) Utilisateur.find("telephone",utilisateur.telephone).firstResult();
        if (utilisateur == null) {
            //
            utilisateur.persist();
            //
            return Response.ok(utilisateur).build();
        }
        return Response.status(300).build();
    }

}
