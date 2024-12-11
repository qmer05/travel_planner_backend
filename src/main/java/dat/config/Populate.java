package dat.config;

import dat.entities.Country;
import jakarta.persistence.EntityManagerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.ArrayList;
import java.util.List;

public class Populate {
    public static void main(String[] args) {
        // Fetch data from the API

        List<String> countries = List.of(
                "Albania", "Andorra", "Armenia", "Austria", "Azerbaijan", "Belarus",
                "Belgium", "Bosnia%20and%20Herzegovina", "Bulgaria", "Croatia", "Cyprus",
                "Czech%20Republic", "Denmark", "Estonia", "Finland", "France", "Georgia",
                "Germany", "Greece", "Hungary", "Iceland", "Ireland", "Italy", "Kazakhstan",
                "Kosovo", "Latvia", "Liechtenstein", "Lithuania", "Luxembourg", "Malta",
                "Moldova", "Monaco", "Montenegro", "Netherlands", "North%20Macedonia",
                "Norway", "Poland", "Portugal", "Romania", "San%20Marino", "Serbia",
                "Slovakia", "Slovenia", "Spain", "Sweden", "Switzerland", "Turkey",
                "Ukraine", "United%20Kingdom", "Vatican%20City"
        );

        for (String country : countries) {
            try {
                // Create an HttpClient instance with a custom timeout
                HttpClient client = HttpClient.newBuilder()
                        .build();

                // Create a request to fetch countries by region Europe
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("https://restcountries.com/v3.1/name/" + country))
                        .GET()
                        .build();

                // Send the request and get the response
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                // Check the status code and process the response
                if (response.statusCode() == 200) {
                    // Parse the response body (JSON) into a JsonNode object
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode countriesNode = objectMapper.readTree(response.body());

                    // Create an EntityManagerFactory and get the EntityManager
                    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
                    try (var em = emf.createEntityManager()) {
                        em.getTransaction().begin();

                        // Loop through each country in the JSON response and persist them
                        for (JsonNode countryNode : countriesNode) {
                            String name = countryNode.path("name").path("common").asText();
                            List<String> currencies = new ArrayList<>();
                            countryNode.path("currencies").fields().forEachRemaining(entry -> currencies.add(entry.getValue().path("name").asText()));
                            String capital = countryNode.path("capital").isArray() ? countryNode.path("capital").get(0).asText() : "";
                            List<String> languages = new ArrayList<>();
                            countryNode.path("languages").fields().forEachRemaining(entry -> languages.add(entry.getValue().asText()));
                            List<String> borders = new ArrayList<>();
                            countryNode.path("borders").forEach(border -> borders.add(border.asText()));
                            double area = countryNode.path("area").asDouble();
                            String googleMaps = countryNode.path("maps").path("googleMaps").asText();
                            int population = countryNode.path("population").asInt();
                            String flagPng = countryNode.path("flags").path("png").asText();

                            // Create a new Country object
                            Country c = new Country(name, currencies, capital, languages, borders, area, googleMaps, population, flagPng);

                            // Persist the country object into the database
                            em.persist(c);
                        }

                        // Commit the transaction
                        em.getTransaction().commit();
                        System.out.println("Countries have been successfully populated.");
                    }
                } else {
                    System.out.println("GET request failed. Status code: " + response.statusCode());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}