package controllers;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Point;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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
    public Response getAllClassementType(@QueryParam("idCalendrier") Long idCalendrier, @QueryParam("categorie") String categorie,
                                         @QueryParam("journee") int journee) {
        //

        HashMap params = new HashMap();
        params.put("idSaison",idCalendrier);
        params.put("categorie",categorie);
        //
        List<Point> points = Point.find("idSaison =: idSaison and categorie =: categorie", params).list();
        //
        //List<Point> pts = Point.listAll();
        points.forEach((p) -> {
            System.out.println("Points: "+p.idEquipe);
        });
        //
        //List<Point> pts = new LinkedList<>();
        //
        Set<Long> equipes = getEquipes(points);
        //
        return Response.ok(triEquipe(listeFinale(equipes, points, journee))).build();
    }
    private List<Point> listeFinale(Set<Long> equipes, List<Point> pts, int journee) {
        //
        List<Point> points = new LinkedList<>();
        //
        equipes.forEach((e)->{
            //
            AtomicInteger ps = new AtomicInteger();
            AtomicInteger butM = new AtomicInteger();
            AtomicInteger butE = new AtomicInteger();
            //idSaison;
            AtomicReference<Long> idMatch = new AtomicReference<>(0L);
            AtomicReference<Long> idEquipe = new AtomicReference<>(0L);
            AtomicReference<Long> idSaison = new AtomicReference<>(0L);
            AtomicReference<String> equipe = new AtomicReference<>("");
            //
            //AtomicReference<Integer> journee = new AtomicReference<>(0);
            AtomicReference<String> categorie = new AtomicReference<>("");
            //Je parcours maintenant les points
            System.out.println("La taille: "+pts.size());
            pts.forEach((p) -> {
                //
                System.out.println("Equipe égalité 2: "+e+" = "+p.idEquipe+" === "+p.journee+" = "+journee);

            //for (int i = 0 ; i > pts.size() ; i++) {
                //
                //Point p = pts.get(i);
                //
                System.out.println("Equipe égalité: "+e+" = "+p.idEquipe+" === "+p.journee+" = "+journee);
                if(e.equals(p.idEquipe) && (p.journee <= journee)) {
                    ps.set(ps.get() + p.point);
                    butM.set(butM.get() + p.butMarque);
                    butE.set(butE.get() + p.butEncaisse);
                    idMatch.set(p.idMatch);
                    idEquipe.set(p.idEquipe);
                    equipe.set(p.equipe);
                    idSaison.set(p.idSaison);
                    //
                    categorie.set(p.categorie);
                    //
                }
            });
            Point pp = new Point();
            pp.equipe = equipe.get();
            pp.point = ps.get();
            pp.idSaison = idSaison.get();
            pp.idEquipe = idEquipe.get();
            pp.idMatch = idMatch.get();
            pp.butMarque = butM.get();
            pp.journee = journee;
            pp.categorie = categorie.get();
            pp.butEncaisse = butE.get();
            //
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
            System.out.println("idEquipe: "+e.idEquipe);
            equipes.add(e.idEquipe);
        });
        //
        return equipes;
    }
    private List<Point> triEquipe(List<Point> tab) {
        for (int i = 0; i < tab.size() - 1; i++) {
            int index = i;
            for (int j = i + 1; j < tab.size(); j++) {
                if (tab.get(j).point < tab.get(index).point) {
                    index = j;
                }
            }
            //
            Point min = tab.get(index);
            //tab.get(index) = tab.get(i);
            tab.set(index,tab.get(i));
            //tab.get(i) = min;
            tab.set(i, min);
        }
        return tab;
    }
    private List<Point> egaliteDePoints(List<Point> points){
        //
        //List<Point> pts = new LinkedList<>();
        //
        for(int i=0; i < points.size(); i++){
            int index = i;
            boolean egalite = false;
            //
            for(int x=0; x < points.size(); x++){
                //
                if (points.get(x).point == points.get(index).point) {
                    System.out.println("Egalité de point!");
                    egalite = true;
                    if ((points.get(x).butMarque - points.get(x).butEncaisse) < (points.get(index).butMarque - points.get(index).butEncaisse)) {
                        //
                        index = x;
                    }
                }
            }
            //
            if(egalite) {
                //
                Point min = points.get(index);
                //tab.get(index) = tab.get(i);
                points.set(index, points.get(i));
                //tab.get(i) = min;
                points.set(i, min);
                //
            }
        }
        return points;
    }
}
