package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.Response;
import models.Billet;
import models.Match;
import models.rapport.Rapport;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Path("paiement")
public class PaiementController {

    @POST
    @Path("billet")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveBillet(HashMap bil) throws URISyntaxException, IOException, InterruptedException {
        //
        ObjectMapper objectMapper = new ObjectMapper();
        String matchS = objectMapper.writeValueAsString(bil);
        JsonNode billet = objectMapper.readTree(matchS);
        Long id = billet.get("id").asLong();
        //
        String rep = verifOTP(billet.get("otp").asText(), billet.get("referencenumber").asText());
        JsonNode jsonNode = objectMapper.readTree(rep);
        if(jsonNode.get("respcodedesc").asText().contains("Approuvé")) {
            //if(true) {
            //Text a revoire
            List<HashMap> billes = new LinkedList<>();
            //
            Long ids = billet.get("id").asLong();
            //
            Match match = Match.findById(id);
            //
            for (int t = 0; t < billet.get("nombrePlace").asInt(); t++) {
                Billet bi = new Billet();
                bi.idMatch = billet.get("id").asLong();
                bi.qrCode = billet.get("qrcode").asText();
                bi.typePlace = billet.get("place").asText();
                bi.journee = billet.get("journee").asText();
                bi.nomEquipeA = billet.get("nomEquipeA").asText();
                bi.nomEquipeB = billet.get("nomEquipeB").asText();
                bi.date = billet.get("date").asText();
                bi.heure = billet.get("heure").asText();
                bi.stade = billet.get("stade").asText();
                bi.checker = false;
                //----------------------------------------------------------------
                if(billet.get("place").equals("Pourtour")){

                    int nPlace = billet.get("nombrePlace").asInt();

                    match.nombreDePlacesPourtour = match.nombreDePlacesPourtour - nPlace;

                }
                if(billet.get("place").equals("Tribune Lateralle")){
                    int nPlace = billet.get("nombrePlace").asInt();

                    //if(match.nombreDePlacesTribuneLateralle
                    match.nombreDePlacesTribuneLateralle = match.nombreDePlacesTribuneLateralle - nPlace;

                }
                if(billet.get("place").equals("Tribune Honneur")){
                    int nPlace = billet.get("nombrePlace").asInt();

                    //if(match.nombreDePlacesTribuneHonneur
                    match.nombreDePlacesTribuneHonneur = match.nombreDePlacesTribuneHonneur - nPlace;

                }
                if(billet.get("place").equals("Tribune Centrale")){
                    int nPlace = billet.get("nombrePlace").asInt();

                    //if(match.nombreDePlacesTribuneHonneur
                    match.nombreDePlacesTribuneHonneur = match.nombreDePlacesTribuneHonneur - nPlace;

                }
                //----------------------------------------------------------------
                //bi.qrCode = (String) billet.get("qrCode");
                //bi.qrCode = (String) billet.get("qrCode");
                //
                bi.qrCode = bi.qrCode +t;
                //
                bil.put("qrCode",bi.qrCode);
                //
                bi.persist();
                //
                billes.add(bil);
            }
            //
            return Response.ok(billes).build();
        }else{
            //
            return Response.status(404).entity(rep).build();
            //return Response.status(404).entity("rep").build();
        }

    }

    @POST
    @Path("direct")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveDirect(HashMap bil) throws URISyntaxException, IOException, InterruptedException {
        //
        ObjectMapper objectMapper = new ObjectMapper();
        String matchS = objectMapper.writeValueAsString(bil);
        JsonNode billet = objectMapper.readTree(matchS);
        //
        String rep = verifOTP((String) billet.get("otp").asText(), (String) billet.get("referencenumber").asText());
        JsonNode jsonNode = objectMapper.readTree(rep);
        if(jsonNode.get("respcodedesc").asText().contains("Approuvé")) {
            //Text a revoire
            List<HashMap> billes = new LinkedList<>();
            //
            Long id = billet.get("id").asLong();
            //
            Match match = Match.findById(id);
            //
                Billet bi = new Billet();
            bi.idMatch = billet.get("id").asLong();
            bi.qrCode = billet.get("qrcode").asText();
            bi.typePlace = billet.get("place").asText();
            bi.journee = billet.get("journee").asText();
            bi.nomEquipeA = billet.get("nomEquipeA").asText();
            bi.nomEquipeB = billet.get("nomEquipeB").asText();
            bi.date = billet.get("date").asText();
            bi.heure = billet.get("heure").asText();
            bi.stade = billet.get("stade").asText();
            //----------------------------------------------------------------
            bi.qrCode = bi.qrCode;
            bi.checker = false;
            //
            bil.put("qrCode",bi.qrCode);
            //
            bi.persist();
            //
            billes.add(bil);

            //
            return Response.ok(billes).build();
        }else{
            //
            return Response.status(404).entity(rep).build();
        }
    }

    @GET
    @Path("verification")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveEquipe(@QueryParam("otp") String otp ) {
        //
        return Response.ok().build();
    }

    @POST
    @Path("otp")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveEquipe(@QueryParam("telephone") String telephone,
                               @QueryParam("montant") double montant,
                               @QueryParam("devise") String devise, HashMap data) throws IOException, URISyntaxException, InterruptedException {

        ObjectMapper objectMapper = new ObjectMapper();
        String matchS = objectMapper.writeValueAsString(data);
        JsonNode matchSS = objectMapper.readTree(matchS);
        Long id = matchSS.get("id").asLong();
        //
        Match match = Match.findById(id);

        if(data.get("place").equals("Direct")){
            String rep = sendOTP(telephone, devise, montant);
            return Response.ok(rep).build();
        }else {

            //
            if (data.get("place").equals("Pourtour")) {

                int nPlace = matchSS.get("nombrePlace").asInt();

                if (match.nombreDePlacesPourtour >= nPlace) {
                    String rep = sendOTP(telephone, devise, montant);
                    return Response.ok(rep).build();
                } else {
                    return Response.status(404).entity("Plus de place disponible dans le pourtour").build();
                }

            } else if (data.get("place").equals("Tribune Lateralle")) {
                int nPlace = matchSS.get("nombrePlace").asInt();

                if (match.nombreDePlacesTribuneLateralle >= nPlace) {
                    String rep = sendOTP(telephone, devise, montant);
                    return Response.ok(rep).build();
                } else {
                    return Response.status(404).entity("Plus de place disponible dans la tribune lateralle").build();
                }

            } else if (data.get("place").equals("Tribune Honneur")) {
                int nPlace = matchSS.get("nombrePlace").asInt();

                if (match.nombreDePlacesTribuneHonneur >= nPlace) {
                    String rep = sendOTP(telephone, devise, montant);
                    return Response.ok(rep).build();
                } else {
                    return Response.status(404).entity("Plus de place disponible dans la tribune d'honneure").build();
                }

            } else {
                int nPlace = matchSS.get("nombrePlace").asInt();

                if (match.nombreDePlacesTribuneCentrale >= nPlace) {
                    String rep = sendOTP(telephone, devise, montant);
                    return Response.ok(rep).build();
                } else {
                    return Response.status(404).entity("Plus de place disponible dans la tribune centrale").build();
                }
            }
        }

        //
        //return Response.ok(response.body()).build();
    }

    private String verifOTP2(String otp, String rrn) throws URISyntaxException, IOException, InterruptedException {
        //https://test.new.rawbankillico.com:4003/RAWAPIGateway/ecommerce/payment/770013/000007316065
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://new.rawbankillico.com:4004/RAWAPIGateway/ecommerce/payment/"+otp+"/"+rrn+""))
                .header("LogInName", "9d668476741822a134ab199f33f43576304c9d49fcb3c487774954cd5d31cd06")
                .header("Content-Type", "application/json")
                .header("LoginPass", "30b9cd6d7001dbbc0e724a6946725281f87df6877e8bdd43484c53cb7abe1b82")
                .header("Authorization", "Basic MzdjMTM1YWJlMWIxOGFhNDJmNDY0NzVkNDA5NzkzNmQzNTk0YzFjYzQxMDZkZGNlNGZlODM3MzgwNjE3Y2RjZToyZGI0ZjRiYTQzYTUyMzAxOTEyMTJhYjgzOWNiYTY5ZmJiOTNmM2Q0NmUwZjQwNjM3M2M1MjMyZDZjNmUzM2I1")
                .GET().build();
        //
        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
        //
        System.out.println("La reponse 2: "+response.body());
        return response.body();
    }

    private String sendOTP2(String telephone, String devise, double montant ) throws URISyntaxException, IOException, InterruptedException {
        //4004
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://new.rawbankillico.com:4004/RAWAPIGateway/ecommerce/payment"))
                .header("LogInName", "9d668476741822a134ab199f33f43576304c9d49fcb3c487774954cd5d31cd06")
                .header("Content-Type", "application/json")
                .header("LoginPass", "30b9cd6d7001dbbc0e724a6946725281f87df6877e8bdd43484c53cb7abe1b82")
                .header("Authorization", "Basic MzdjMTM1YWJlMWIxOGFhNDJmNDY0NzVkNDA5NzkzNmQzNTk0YzFjYzQxMDZkZGNlNGZlODM3MzgwNjE3Y2RjZToyZGI0ZjRiYTQzYTUyMzAxOTEyMTJhYjgzOWNiYTY5ZmJiOTNmM2Q0NmUwZjQwNjM3M2M1MjMyZDZjNmUzM2I1")
                .POST(HttpRequest.BodyPublishers.ofString("{\r\n\t\"mobilenumber\": \""+telephone+"\",\r\n\t\"trancurrency\":\""+devise+"\",\r\n\t\"amounttransaction\": \""+montant+"\",\r\n\t\"merchantid\": \"brnch0000000000000801\",\r\n\t\"invoiceid\":\"123456715\",\r\n\t\"terminalid\":\"123456789012\",\r\n\t\"encryptkey\": \"AX8dsXSKqWlJqRhpnCeFJ03CzqMsCisQVUNSymXKqeiaQdHf8eQSyITvCD6u3CLZJBebnxj5LbdosC/4OvUtNbAUbaIgBKMC5MpXGRXZdfAlGsVRfHTmjaGDe1RIiHKP\",\r\n\t\"securityparams\":{\r\n\t\t\"gpslatitude\": \"24.864190\",\r\n\t\t\"gpslongitude\": \"67.090420\"\r\n\t}\r\n}"))
                .build();
        //
        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
        //
        System.out.println("La reponse 1: "+response.body());
        return response.body();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private String verifOTP(String otp, String rrn) throws URISyntaxException, IOException, InterruptedException {
        //https://test.new.rawbankillico.com:4003/RAWAPIGateway/ecommerce/payment/770013/000007316065
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://new.rawbankillico.com:4004/RAWAPIGateway/ecommerce/payment/"+otp+"/"+rrn+""))
                .header("LogInName", "9d668476741822a134ab199f33f43576304c9d49fcb3c487774954cd5d31cd06")
                .header("LogInPass", "30b9cd6d7001dbbc0e724a6946725281f87df6877e8bdd43484c53cb7abe1b82")
                .header("Authorization", "Basic MzdjMTM1YWJlMWIxOGFhNDJmNDY0NzVkNDA5NzkzNmQzNTk0YzFjYzQxMDZkZGNlNGZlODM3MzgwNjE3Y2RjZToyZGI0ZjRiYTQzYTUyMzAxOTEyMTJhYjgzOWNiYTY5ZmJiOTNmM2Q0NmUwZjQwNjM3M2M1MjMyZDZjNmUzM2I1")
                .GET().build();
        //
        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
        //
        System.out.println("La reponse 2: "+response.body());
        return response.body();
    }

    private String sendOTP(String telephone, String devise, double montant ) throws URISyntaxException, IOException, InterruptedException {
        //4004
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://new.rawbankillico.com:4004/RAWAPIGateway/ecommerce/payment"))
                .header("LogInName", "9d668476741822a134ab199f33f43576304c9d49fcb3c487774954cd5d31cd06")
                .header("LogInPass", "30b9cd6d7001dbbc0e724a6946725281f87df6877e8bdd43484c53cb7abe1b82")
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic MzdjMTM1YWJlMWIxOGFhNDJmNDY0NzVkNDA5NzkzNmQzNTk0YzFjYzQxMDZkZGNlNGZlODM3MzgwNjE3Y2RjZToyZGI0ZjRiYTQzYTUyMzAxOTEyMTJhYjgzOWNiYTY5ZmJiOTNmM2Q0NmUwZjQwNjM3M2M1MjMyZDZjNmUzM2I1")
                .POST(HttpRequest.BodyPublishers.ofString("{\r\n\t\"mobilenumber\": \""+telephone+"\",\r\n\t\"trancurrency\":\""+devise+"\",\r\n\t\"amounttransaction\": \""+montant+"\",\r\n\t\"merchantid\": \"brnch0000000000000801\",\r\n\t\"invoiceid\":\"123456715\",\r\n\t\"terminalid\":\"123456789012\",\r\n\t\"encryptkey\": \"AX8dsXSKqWlJqRhpnCeFJ03CzqMsCisQVUNSymXKqeiaQdHf8eQSyITvCD6u3CLZJBebnxj5LbdosC/4OvUtNbAUbaIgBKMC5MpXGRXZdfAlGsVRfHTmjaGDe1RIiHKP\",\r\n\t\"securityparams\":{\r\n\t\t\"gpslatitude\": \"24.864190\",\r\n\t\t\"gpslongitude\": \"67.090420\"\r\n\t}\r\n}"))
                //.POST(HttpRequest.BodyPublishers.ofString("{\r\n\t\"mobilenumber\": \""+telephone+"\",\r\n\t\"trancurrency\":\""+devise+"\",\r\n\t\"amounttransaction\": \""+montant+"\",\r\n\t\"merchantid\": \"brnch0000000000000801\",\r\n\t\"invoiceid\":\"123456715\",\r\n\t\"terminalid\":\"123456789012\",\r\n\t\"encryptkey\": \"AX8dsXSKqWlJqRhpnCeFJ03CzqMsCisQVUNSymXKqeiaQdHf8eQSyITvCD6u3CLZJBebnxj5LbdosC/4OvUtNbAUbaIgBKMC5MpXGRXZdfAlGsVRfHTmjaGDe1RIiHKP\",\r\n\t\"securityparams\":{\r\n\t\t\"gpslatitude\": \"24.864190\",\r\n\t\t\"gpslongitude\": \"67.090420\"\r\n\t}\r\n}"))
                .build();
        //
        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
        //
        System.out.println("La reponse 1: "+response.body());
        return response.body();
    }


}
