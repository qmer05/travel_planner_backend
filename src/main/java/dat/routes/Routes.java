package dat.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final CountryRoute countryRoute = new CountryRoute();

    public EndpointGroup getRoutes() {
        return () -> {
                path("/countries", countryRoute.getRoutes());
        };
    }
}
