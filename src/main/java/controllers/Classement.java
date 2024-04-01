package controllers;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Equipe;
import models.Match;
import models.Point;

import java.util.*;

@Path("classement")
public class Classement {

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Point> points = Point.listAll();
        return Response.ok(points).build();
    }

    @GET
    @Path("allClassement")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllClassementType(@QueryParam("idCalendrier") Long idCalendrier, @QueryParam("categorie") String categorie) {
        //

        //
        List<Point> points = Point.listAll();
        //
        List<Point> pts = new LinkedList<>();
        //
        points.forEach((e)->{
            //
            HashMap params = new HashMap();
            params.put("idCalendrier",idCalendrier);
            params.put("categorie",categorie);
            //categorie==playoff
            Match match = (Match) Match.find("idCalendrier =: idCalendrier and categorie =: categorie", params).firstResult();
            //
            if (match != null) {
                if (match.id.equals(e.idMatch)) {
                    pts.add(e);
                }
            }
        });
        //Le classmenent par points


        return Response.ok(points).build();
    }

    private List<Point> listeFinale(Set<Long> equipes, List<Point> pts) {
        //
        List<Point> points = new LinkedList<>();
        //
        equipes.forEach((e)->{
            //
            int ps = 0;
            int butM = 0;
            int butE = 0;
            //idSaison;
            Long idMatch = 0L;
            Long idEquipe = 0L;
            Long idSaison = 0L;
            String equipe = "";
            //Je parcours maintenant les points
            for (Point p : pts) {
                //
                if(e.equals(p.idEquipe)) {
                    ps = ps + p.point;
                    butM = butM + p.butMarque;
                    butE = butE + p.butEncaisse;
                    idMatch = p.idMatch;
                    idEquipe = p.idEquipe;
                    equipe = p.equipe;
                    idSaison = p.idSaison;
                }
            }
            Point pp = new Point();
            pp.equipe = equipe;
            pp.point = ps;
            pp.idSaison = idSaison;
            pp.idEquipe = idEquipe;
            pp.idMatch = idMatch;
            pp.butMarque = butM;
            pp.butEncaisse = butE;

            points.add(pp);
        });
        //
        return points;
    }

    private Set<Long> getEquipes(List<Point> tabs) {
        Set<Long> equipes =new HashSet<>();
        //
        tabs.forEach((e)->{
            //
            equipes.add(e.idEquipe);
        });
        //
        return equipes;
    }
    private List triEquipe(List<Point> tab) {
        for (int i = 0; i < tab.size() - 1; i++) {
            int index = i;
            for (int j = i + 1; j < tab.size(); j++) {
                if (tab.get(j).point < tab.get(index).point) {
                    index = j;
                }
            }

            Point min = tab.get(index);
            //tab.get(index) = tab.get(i);
            tab.set(index,tab.get(i));
            //tab.get(i) = min;
            tab.set(i, min);
        }
        return tab;
    }


}
