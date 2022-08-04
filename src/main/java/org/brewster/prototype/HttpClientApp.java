package org.brewster.prototype;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientApp {


    public static void main(String[] args) {
        HttpClientApp ourApp;
        HttpRequest aGetRequest;
        HttpRequest aCreateRequest;
        HttpClient theClient;
        HttpResponse<String> aResponse;
        Loan aLoan;
        ObjectMapper objectMapper = new ObjectMapper();

        ourApp = new HttpClientApp();
        try {
            aGetRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/LSDS_Brewster_RestService-1.0-SNAPSHOT/api/loan?id=100"))
                    .GET()
                    .build();

            theClient = HttpClient.newBuilder()
                    .build();

            aResponse = theClient.send(aGetRequest, HttpResponse.BodyHandlers.ofString());
            //Response here should be that we can't find the loan if this is first run.
            // Subsequent runs might actually find this if server still running
            System.out.println("Here is response to get loan id 100: " + aResponse.body().toString());

            // add our first loan
            aLoan = new Loan(0,"100,000.00","6%","60","10,321.00");
            aCreateRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/LSDS_Brewster_RestService-1.0-SNAPSHOT/api/loan/create"))
                    .headers("Content-Type","application/json")
                    .POST(HttpRequest.BodyPublishers
                       .ofString(objectMapper.writeValueAsString(aLoan)))
                    .build();
            aResponse = theClient.send(aCreateRequest,HttpResponse.BodyHandlers.ofString());
            //Response here should be success
            System.out.println("Response to create loan: " + aResponse.body().toString());

            // add our second loan
            aLoan = new Loan(0,"150,000.00","5%","50","15,321.00");
            aCreateRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/LSDS_Brewster_RestService-1.0-SNAPSHOT/api/loan/create"))
                    .headers("Content-Type","application/json")
                    .POST(HttpRequest.BodyPublishers
                            .ofString(objectMapper.writeValueAsString(aLoan)))
                    .build();
            aResponse = theClient.send(aCreateRequest,HttpResponse.BodyHandlers.ofString());
            //Response here should be success
            System.out.println("Response to create loan: " + aResponse.body().toString());

            // add our third loan
            aLoan = new Loan(0,"190,000.00","4%","70","17,321.00");
            aCreateRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/LSDS_Brewster_RestService-1.0-SNAPSHOT/api/loan/create"))
                    .headers("Content-Type","application/json")
                    .POST(HttpRequest.BodyPublishers
                            .ofString(objectMapper.writeValueAsString(aLoan)))
                    .build();
            aResponse = theClient.send(aCreateRequest,HttpResponse.BodyHandlers.ofString());
            //Response here should be success
            System.out.println("Response to create loan: " + aResponse.body().toString());

            //Now let's update the loan we just created
            aLoan = ourApp.buildLoanFromResponseBody(aResponse.body().toString());
            aLoan.setAmount("220,000.00");
            aCreateRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/LSDS_Brewster_RestService-1.0-SNAPSHOT/api/loan/update"))
                    .headers("Content-Type","application/json")
                    .POST(HttpRequest.BodyPublishers
                            .ofString(objectMapper.writeValueAsString(aLoan)))
                    .build();
            aResponse = theClient.send(aCreateRequest,HttpResponse.BodyHandlers.ofString());
            //Response here should be success
            System.out.println("Response to update loan: " + aResponse.body().toString());

            //Try that again, but this time we expect it should fail since loan not complete
            aLoan.setAmount("");
            aCreateRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/LSDS_Brewster_RestService-1.0-SNAPSHOT/api/loan/update"))
                    .headers("Content-Type","application/json")
                    .POST(HttpRequest.BodyPublishers
                            .ofString(objectMapper.writeValueAsString(aLoan)))
                    .build();
            aResponse = theClient.send(aCreateRequest,HttpResponse.BodyHandlers.ofString());
            //Response here should be success
            System.out.println("Response to update loan: " + aResponse.body().toString());
        }
        catch(URISyntaxException ex){
            System.out.println(ex.getMessage());
        }
        catch (IOException|InterruptedException ex){
            System.out.println(ex.getMessage());
        }
    }

    public Loan buildLoanFromResponseBody(String body){
        Loan aLoan = new Loan();
        String[] parts;
        int pointer;
        parts = body.split("\\|");

        pointer = parts[0].indexOf('=')+2;
        aLoan.setId(Integer.valueOf(parts[0].substring(pointer)));
        pointer = parts[1].indexOf('=')+2;
        aLoan.setAmount(parts[1].substring(pointer));
        pointer = parts[2].indexOf('=')+2;
        aLoan.setRate(parts[2].substring(pointer));
        pointer = parts[3].indexOf('=')+2;
        aLoan.setTerm(parts[3].substring(pointer));
        pointer = parts[4].indexOf('=')+2;
        aLoan.setPayment(parts[4].substring(pointer));

        return aLoan;
    }
}
