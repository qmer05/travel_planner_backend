package dat.dtos;

import dat.entities.Country;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class CountryDTO {

    private Long id;
    private String name;
    private List<String> currencies;
    private String capital;
    private List<String> languages;
    private List<String> borders;
    private double area;
    private String googleMaps;
    private int population;
    private String flagPng;

    public CountryDTO(Country country) {
        this.id = country.getId();
        this.name = country.getName();
        this.currencies = country.getCurrencies();
        this.capital = country.getCapital();
        this.languages = country.getLanguages();
        this.borders = country.getBorders();
        this.area = country.getArea();
        this.googleMaps = country.getGoogleMaps();
        this.population = country.getPopulation();
        this.flagPng = country.getFlagPng();
    }

    public CountryDTO(String name, List<String> currencies, String capital, List<String> languages, List<String> borders, double area, String googleMaps, int population, String flagPng) {
        this.name = name;
        this.currencies = currencies;
        this.capital = capital;
        this.languages = languages;
        this.borders = borders;
        this.area = area;
        this.googleMaps = googleMaps;
        this.population = population;
        this.flagPng = flagPng;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CountryDTO that = (CountryDTO) o;
        return Double.compare(area, that.area) == 0 && population == that.population && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(currencies, that.currencies) && Objects.equals(capital, that.capital) && Objects.equals(languages, that.languages) && Objects.equals(borders, that.borders) && Objects.equals(googleMaps, that.googleMaps) && Objects.equals(flagPng, that.flagPng);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, currencies, capital, languages, borders, area, googleMaps, population, flagPng);
    }
}
