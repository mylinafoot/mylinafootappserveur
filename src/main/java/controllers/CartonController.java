package controllers;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Carton;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/carton")
public class CartonController {
    @GET
    @Path("jaune")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCartonsJaunes() {
        List<Carton> cartons = Carton.listAll(); // Récupération de tous les cartons
        Map<Long, Map<Long, Integer>> yellowCardsByPlayer = new HashMap<>();

        for (Carton carton : cartons) {
            if ("jaune".equalsIgnoreCase(carton.typecarton)) {
                yellowCardsByPlayer
                        .computeIfAbsent(carton.idjoueur, k -> new HashMap<>())
                        .merge(carton.idMatch, 1, Integer::sum);
            }
        }

        return Response.ok(yellowCardsByPlayer).build();
    }
}
