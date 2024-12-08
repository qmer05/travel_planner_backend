package dat.routes;

import dat.controllers.impl.QuestionController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class QuestionRoute {

    private final QuestionController questionController = new QuestionController();

    protected EndpointGroup getRoutes() {

        return () -> {
            post("/", questionController::create, Role.USER);
            get("/", questionController::readAll);
            get("/{id}", questionController::read);
            put("/{id}", questionController::update);
            delete("/{id}", questionController::delete);
        };
    }
}
