package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.Response;
import models.Billet;
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
    public Response saveBillet(HashMap billet) throws URISyntaxException, IOException, InterruptedException {
        //
        ObjectMapper obj = new ObjectMapper();
        //
        String rep = verifOTP((String) billet.get("otp"), (String) billet.get("rrn"));
        JsonNode jsonNode = obj.readTree(rep);
        if(jsonNode.get("respcodedesc").equals("Transaction éffectué")) {
            //Text a revoire
            List<HashMap> billes = new LinkedList<>();
            //
            for (int t = 0; t < (Integer) billet.get("nombrePlace"); t++) {
                Billet bi = new Billet();
                bi.idMatch = (Long) billet.get("id");
                bi.qrCode = (String) billet.get("qrCode");
                bi.typePlace = (String) billet.get("qrCode");
                //bi.Code = (String) billet.get("qrCode");
                bi.qrCode = (String) billet.get("qrCode");
                bi.qrCode = (String) billet.get("qrCode");
                //
                bi.qrCode = bi.qrCode +""+t;
                //
                bi.persist();
                //
                billes.add(billet);
            }
            //
            return Response.ok(billes).build();
        }else{
            //
            return Response.status(404).entity(rep).build();
        }

    }

    @POST
    @Path("direct")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveDirect(HashMap direct) {
        //
        //Unirest.setTimeouts(0, 0);
        //
        return Response.ok().build();
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

    @GET
    @Path("otp")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveEquipe(@QueryParam("telephone") String telephone,
                               @QueryParam("montant") double montant,
                               @QueryParam("devise") String devise) throws IOException, URISyntaxException, InterruptedException {
        /*
        //Je vérifie d'abord si le nombre est bon avant lancer la requete.
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        var mediaType = okhttp3.MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n\t\"mobilenumber\": \"00243815381693\",\r\n\t\"trancurrency\":\"USD\",\r\n\t\"amounttransaction\": \"5.00\",\r\n\t\"merchantid\": \"merch0000000000001042\",\r\n\t\"invoiceid\":\"123456715\",\r\n\t\"terminalid\":\"123456789012\",\r\n\t\"encryptkey\": \"NozZSGL660ZZM8u4kUTV4CfgSy3G7wpFDQ0vCOhLWLpmnkNLkGia6mn7J2j2f4CJ/RDKF0ICxN7mBD9ciURYWj97KT2LYBoaPJVJs3hv5s5SGYoOw4fcAigt7+0nQiza\",\r\n\t\"securityparams\":{\r\n\t\t\"gpslatitude\": \"24.864190\",\r\n\t\t\"gpslongitude\": \"67.090420\"\r\n\t}\r\n}");
        Request request = new Request.Builder()
                .url("https://test.new.rawbankillico.com:4003/RAWAPIGateway/ecommerce/payment")
                .method("POST", body)
                .addHeader("LogInName", "a5169891f7424defec80033e2c4264004716e4846b6929caea8f431c7568d604")
                .addHeader("Content-Type", "application/json")
                .addHeader("LoginPass", "22cf830393691407806b22424dd66354e543c0e34f8161f00df1e74fa0a61e2b")
                .addHeader("Authorization", "Basic ZGVsdGE6MTIzNDU2")
                .build();
        okhttp3.Response response = client.newCall(request).execute();
        //

         */
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://test.new.rawbankillico.com:4003/RAWAPIGateway/ecommerce/payment"))
                .header("LogInName", "a5169891f7424defec80033e2c4264004716e4846b6929caea8f431c7568d604")
                .header("Content-Type", "application/json")
                .header("LoginPass", "22cf830393691407806b22424dd66354e543c0e34f8161f00df1e74fa0a61e2b")
                .header("Authorization", "Basic ZGVsdGE6MTIzNDU2")
                .POST(HttpRequest.BodyPublishers.ofString("{\r\n\t\"mobilenumber\": \""+telephone+"\",\r\n\t\"trancurrency\":\""+devise+"\",\r\n\t\"amounttransaction\": \""+montant+"\",\r\n\t\"merchantid\": \"merch0000000000001042\",\r\n\t\"invoiceid\":\"123456715\",\r\n\t\"terminalid\":\"123456789012\",\r\n\t\"encryptkey\": \"NozZSGL660ZZM8u4kUTV4CfgSy3G7wpFDQ0vCOhLWLpmnkNLkGia6mn7J2j2f4CJ/RDKF0ICxN7mBD9ciURYWj97KT2LYBoaPJVJs3hv5s5SGYoOw4fcAigt7+0nQiza\",\r\n\t\"securityparams\":{\r\n\t\t\"gpslatitude\": \"24.864190\",\r\n\t\t\"gpslongitude\": \"67.090420\"\r\n\t}\r\n}"))
                .build();
        //
        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
        //
        System.out.println("La reponse 1: "+response.body());
        //
        return Response.ok(response.body()).build();
    }

    private String verifOTP(String otp, String rrn) throws URISyntaxException, IOException, InterruptedException {
        //https://test.new.rawbankillico.com:4003/RAWAPIGateway/ecommerce/payment/770013/000007316065
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://test.new.rawbankillico.com:4003/RAWAPIGateway/ecommerce/payment/"+otp+"/"+rrn+""))
                .header("LogInName", "a5169891f7424defec80033e2c4264004716e4846b6929caea8f431c7568d604")
                .header("Content-Type", "application/json")
                .header("LogInPass", "22cf830393691407806b22424dd66354e543c0e34f8161f00df1e74fa0a61e2b")
                .header("Authorization", "Basic ZGVsdGE6MTIzNDU2")
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

}
