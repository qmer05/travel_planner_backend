package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.CountryDTO;
import dat.entities.Country;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class CountryDAO implements IDAO<CountryDTO, Integer> {

    private static CountryDAO instance;
    private static EntityManagerFactory emf;

    public static CountryDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CountryDAO();
        }
        return instance;
    }

    @Override
    public CountryDTO read(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Country country = em.find(Country.class, integer);
            return new CountryDTO(country);
        }
    }

    @Override
    public List<CountryDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<CountryDTO> query = em.createQuery("SELECT new dat.dtos.CountryDTO(q) FROM Country q", CountryDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public CountryDTO create(CountryDTO countryDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Country country = new Country(countryDTO);
            em.persist(country);
            em.getTransaction().commit();
            return new CountryDTO(country);
        }
    }

    @Override
    public CountryDTO update(Integer integer, CountryDTO countryDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Country country = em.find(Country.class, integer);
            country.setName(countryDTO.getName());
            country.setCapital(countryDTO.getCapital());
            country.setLanguages(countryDTO.getLanguages());
            country.setCurrencies(countryDTO.getCurrencies());
            country.setArea(countryDTO.getArea());
            country.setPopulation(countryDTO.getPopulation());
            country.setFlagPng(countryDTO.getFlagPng());
            country.setGoogleMaps(countryDTO.getGoogleMaps());
            Country mergedCountry = em.merge(country);
            em.getTransaction().commit();
            return mergedCountry != null ? new CountryDTO(mergedCountry) : null;
        }
    }

    @Override
    public void delete(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Country country = em.find(Country.class, integer);
            if (country != null) {
                em.remove(country);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Country country = em.find(Country.class, integer);
            return country != null;
        }
    }
}
