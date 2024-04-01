package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Utilisateur;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

@Path("utilisateur")
public class UtilisateurController {

    @GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setLoger(@QueryParam("telephone") String telephone,
                             @QueryParam("code") String code) throws IOException, URISyntaxException, InterruptedException {
        //
        HashMap params = new HashMap();
        params.put("token", "HG59P642KW9AQ2M");
        params.put("to", telephone);
        params.put("from", "MYLINAFOOT");
        params.put("message", code);
        //
        ObjectMapper obj = new ObjectMapper();
        String data = obj.writeValueAsString(params);
        //
        Utilisateur utilisateur = (Utilisateur) Utilisateur.find("telephone",telephone).firstResult();
        if(utilisateur != null){
            Utilisateur utilisateur1 = new Utilisateur();
            utilisateur1.telephone = telephone;
            utilisateur1.persist();
        }
        //
        String reponse = veriSMS(data);
        //
        return Response.ok(reponse).build();

    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response enregistrerUtilisateur(Utilisateur utilisateur){
        //

        Utilisateur utilisateur1 = (Utilisateur) Utilisateur.find(utilisateur.telephone).firstResult();
        if (utilisateur1 == null) {
            //
            utilisateur1.pays = utilisateur.pays;
            utilisateur1.province = utilisateur.province;
            utilisateur1.nomUtilisateur = utilisateur.nomUtilisateur;
            //
            return Response.ok(utilisateur).build();
        }
        return Response.status(404).build();
    }

    private String veriSMS(String data) throws URISyntaxException, IOException, InterruptedException {
        //https://test.new.rawbankillico.com:4003/RAWAPIGateway/ecommerce/payment/770013/000007316065
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.keccel.com/sms/message.asp"))
                .POST(HttpRequest.BodyPublishers.ofString(data))
                //.POST(HttpRequest.BodyPublishers.ofString("{\r\n\t\"mobilenumber\": \""+telephone+"\",\r\n\t\"trancurrency\":\""+devise+"\",\r\n\t\"amounttransaction\": \""+montant+"\",\r\n\t\"merchantid\": \"brnch0000000000000801\",\r\n\t\"invoiceid\":\"123456715\",\r\n\t\"terminalid\":\"123456789012\",\r\n\t\"encryptkey\": \"AX8dsXSKqWlJqRhpnCeFJ03CzqMsCisQVUNSymXKqeiaQdHf8eQSyITvCD6u3CLZJBebnxj5LbdosC/4OvUtNbAUbaIgBKMC5MpXGRXZdfAlGsVRfHTmjaGDe1RIiHKP\",\r\n\t\"securityparams\":{\r\n\t\t\"gpslatitude\": \"24.864190\",\r\n\t\t\"gpslongitude\": \"67.090420\"\r\n\t}\r\n}"))
                .build();
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


}
