package controllers;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Agent;
import models.Stade;

import java.util.HashMap;
import java.util.List;

@Path("agent")
public class AgentController {

    @GET
    @Path("one")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@QueryParam("id") Long id) {
        Agent agent = Agent.findById(id);
        return Response.ok(agent).build();
    }
    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEquipe() {
        List<Agent> agents = Agent.listAll();
        return Response.ok(agents).build();
    }

    //
    @GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@QueryParam("telephone") String telephone, @QueryParam("mdp") String mdp) {
        HashMap params = new HashMap();
        params.put("telephone",telephone);
        params.put("mdp",mdp);
        //
        Agent agent = (Agent) Agent.find("telephone =: telephone and mdp =: mdp",params).firstResult();
        return Response.ok(agent).build();
    }

    //
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveEquipe(Agent agent) {
        agent.persist();
        return Response.ok(agent).build();
    }

    //
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateEquipe(Agent agent) {
        Agent agent1 = Agent.findById(agent.id);
        if(agent1 == null){
            return  Response.status(404).build();
        }

        agent1.nom = agent.nom;
        agent1.postnom = agent.postnom;
        agent1.prenom = agent.prenom;
        agent1.telephone = agent.telephone;
        agent1.mdp = agent.mdp;

        return Response.ok(agent1).build();
    }

    //
    @DELETE
    //@Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response removeEquipe(@QueryParam("id") Long id) {
        Agent.deleteById(id);
        return Response.ok().build();
    }

    //________________________________________________________________

}
