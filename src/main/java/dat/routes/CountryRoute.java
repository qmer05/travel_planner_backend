package dat.routes;

import dat.controllers.impl.CountryController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class CountryRoute {

    private final CountryController countryController = new CountryController();

    protected EndpointGroup getRoutes() {

        return () -> {
            // New route to populate countries
            get("/populate", countryController::populateCountries); // No authentication required here

            post("/", countryController::create, Role.USER);
            get("/", countryController::readAll);
            get("/{id}", countryController::read);
            put("/{id}", countryController::update);
            delete("/{id}", countryController::delete);
        };
    }
}
