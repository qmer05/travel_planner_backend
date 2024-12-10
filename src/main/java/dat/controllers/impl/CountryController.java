package dat.controllers.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.CountryDAO;
import dat.dtos.CountryDTO;
import dat.entities.Country;
import io.javalin.http.Context;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class CountryController implements IController<CountryDTO, Integer> {

    private final CountryDAO dao;

    public CountryController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = CountryDAO.getInstance(emf);
    }

    @Override
    public void read(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // DTO
        CountryDTO countryDTO = dao.read(id);
        // response
        ctx.res().setStatus(200);
        ctx.json(countryDTO, CountryDTO.class);
    }

    @Override
    public void readAll(Context ctx) {
        // List of DTOS
        List<CountryDTO> countryDTOS = dao.readAll();
        // response
        ctx.res().setStatus(200);
        ctx.json(countryDTOS, CountryDTO.class);
    }

    @Override
    public void create(Context ctx) {
        // request
        CountryDTO jsonRequest = ctx.bodyAsClass(CountryDTO.class);
        // DTO
        CountryDTO countryDTO = dao.create(jsonRequest);
        // response
        ctx.res().setStatus(201);
        ctx.json(countryDTO, CountryDTO.class);
    }

    @Override
    public void update(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // dto
        CountryDTO countryDTO = dao.update(id, validateEntity(ctx));
        // response
        ctx.res().setStatus(200);
        ctx.json(countryDTO, Country.class);
    }

    @Override
    public void delete(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        dao.delete(id);
        // response
        ctx.res().setStatus(204);
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        return dao.validatePrimaryKey(integer);
    }

    @Override
    public CountryDTO validateEntity(Context ctx) {
        return ctx.bodyValidator(CountryDTO.class)
                .check(country -> country.getName() != null && !country.getName().isEmpty(), "Name must be set")
                .check(country -> country.getCapital() != null && !country.getCapital().isEmpty(), "Capital must be set")
                .check(country -> country.getLanguages() != null && !country.getLanguages().isEmpty(), "At least one language must be set")
                .check(country -> country.getCurrencies() != null && !country.getCurrencies().isEmpty(), "At least one currency must be set")
                .check(country -> country.getArea() > 0, "Area must be greater than 0")
                .check(country -> country.getPopulation() >= 0, "Population must be a positive number or zero")
                .check(country -> country.getFlagPng() != null && !country.getFlagPng().isEmpty(), "Flag PNG URL must be set")
                .get(); // Return the validated DTO
    }

    public void populateCountries(Context ctx) {
        try {
            // Create an HttpClient instance
            HttpClient client = HttpClient.newHttpClient();

            // Create a request to fetch countries by region Europe
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://restcountries.com/v3.1/region/europe"))
                    .GET()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check the status code and process the response
            if (response.statusCode() == 200) {
                // Parse the response body (JSON) into a JsonNode object
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode countriesNode = objectMapper.readTree(response.body());

                // Start a database transaction to insert countries
                try (EntityManager em = HibernateConfig.getEntityManagerFactory().createEntityManager()) {
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
                        Country country = new Country(name, currencies, capital, languages, borders, area, googleMaps, population, flagPng);

                        // Persist the country object into the database
                        em.persist(country);
                    }

                    // Commit the transaction
                    em.getTransaction().commit();
                    ctx.result("Countries have been successfully populated.");
                } catch (Exception e) {
                    ctx.status(500).result("Error populating countries: " + e.getMessage());
                }
            } else {
                ctx.status(response.statusCode()).result("Failed to fetch country data from the API.");
            }
        } catch (Exception e) {
            ctx.status(500).result("Error fetching country data: " + e.getMessage());
        }
    }
}
