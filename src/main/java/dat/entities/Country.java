package dat.entities;

import dat.dtos.CountryDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "countries")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String name;

    @Setter
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "country_currencies", joinColumns = @JoinColumn(name = "country_id"))
    @Column(name = "currency")
    private List<String> currencies;  // Storing multiple currencies in a list

    @Setter
    private String capital;

    @Setter
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "country_languages", joinColumns = @JoinColumn(name = "country_id"))
    @Column(name = "language")
    private List<String> languages;  // Storing multiple languages in a list

    @Setter
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "country_borders", joinColumns = @JoinColumn(name = "country_id"))
    @Column(name = "border")
    private List<String> borders;  // Storing country borders

    @Setter
    private double area;
    @Setter
    private String googleMaps;
    @Setter
    private int population;
    @Setter
    private String flagPng;

    public Country(String name, List<String> currencies, String capital, List<String> languages, List<String> borders, double area, String googleMaps, int population, String flagPng) {
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

    public Country(CountryDTO countryDTO) {
        this.id = countryDTO.getId();
        this.name = countryDTO.getName();
        this.currencies = countryDTO.getCurrencies();
        this.capital = countryDTO.getCapital();
        this.languages = countryDTO.getLanguages();
        this.borders = countryDTO.getBorders();
        this.area = countryDTO.getArea();
        this.googleMaps = countryDTO.getGoogleMaps();
        this.population = countryDTO.getPopulation();
        this.flagPng = countryDTO.getFlagPng();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return Double.compare(area, country.area) == 0 && population == country.population && Objects.equals(id, country.id) && Objects.equals(name, country.name) && Objects.equals(currencies, country.currencies) && Objects.equals(capital, country.capital) && Objects.equals(languages, country.languages) && Objects.equals(borders, country.borders) && Objects.equals(googleMaps, country.googleMaps) && Objects.equals(flagPng, country.flagPng);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, currencies, capital, languages, borders, area, googleMaps, population, flagPng);
    }
}
