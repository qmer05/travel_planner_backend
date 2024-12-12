package dat.routes;

import dat.controllers.impl.CountryController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class CountryRoute {

    private final CountryController countryController = new CountryController();

    protected EndpointGroup getRoutes() {

        return () -> {
            post("/", countryController::create, Role.ADMIN);
            get("/", countryController::readAll, Role.ANYONE);
            get("/{id}", countryController::read, Role.ADMIN);
            put("/{id}", countryController::update, Role.ADMIN);
            delete("/{id}", countryController::delete, Role.ADMIN);
        };
    }
}
