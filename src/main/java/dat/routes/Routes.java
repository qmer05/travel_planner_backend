package dat.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final QuestionRoute questionRoute = new QuestionRoute();

    public EndpointGroup getRoutes() {
        return () -> {
                path("/questions", questionRoute.getRoutes());
        };
    }
}
