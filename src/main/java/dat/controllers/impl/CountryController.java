package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.CountryDAO;
import dat.dtos.CountryDTO;
import dat.entities.Country;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

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
}
