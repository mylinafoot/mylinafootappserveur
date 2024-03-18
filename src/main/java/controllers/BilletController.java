package controllers;


import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Agent;
import models.Arbitre;
import models.Billet;

import java.util.HashMap;
import java.util.List;

@Path("billet")
public class BilletController {


    //check/idAgent?idAgent=${e['idAgent']}&qrcode=${e['qrcode']}
    @GET
    @Path("check")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response check(@QueryParam("idAgent") Long idAgent, @QueryParam("qrcode") String qrcode) {
        //HashMap params = new HashMap();
        //params.put("telephone",telephone);
        //params.put("mdp",mdp);
        //
        Billet billet = (Billet) Billet.find("telephone","").firstResult();
        if(billet != null){
            //
            if(billet.checker){
                return Response.status(200).entity("Ce billet a déjà été scanné !"+billet.typePlace).build();
            }else{
                billet.checker = true;
                billet.idAgent = idAgent;
                return Response.status(200).entity("Bienvenu et bon match à vous! \n"+billet.typePlace).build();
            }
        }else{
            return Response.status(404).entity("Ce billet n'est enregistré dans la base de donné.").build();
        }

    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response allBillet() {
        //HashMap params = new HashMap();
        //params.put("telephone",telephone);
        //params.put("mdp",mdp);
        //
        List<Billet> billets = Billet.listAll();
          return Response.ok(billets).build();
    }

}
