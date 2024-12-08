package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.QuestionDAO;
import dat.dtos.QuestionDTO;
import dat.entities.Question;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class QuestionController implements IController<QuestionDTO, Integer> {

    private final QuestionDAO dao;

    public QuestionController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = QuestionDAO.getInstance(emf);
    }

    @Override
    public void read(Context ctx)  {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // DTO
        QuestionDTO questionDTO = dao.read(id);
        // response
        ctx.res().setStatus(200);
        ctx.json(questionDTO, QuestionDTO.class);
    }

    @Override
    public void readAll(Context ctx) {
        // List of DTOS
        List<QuestionDTO> questionDTOS = dao.readAll();
        // response
        ctx.res().setStatus(200);
        ctx.json(questionDTOS, QuestionDTO.class);
    }

    @Override
    public void create(Context ctx) {
        // request
        QuestionDTO jsonRequest = ctx.bodyAsClass(QuestionDTO.class);
        // DTO
        QuestionDTO questionDTO = dao.create(jsonRequest);
        // response
        ctx.res().setStatus(201);
        ctx.json(questionDTO, QuestionDTO.class);
    }

    @Override
    public void update(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // dto
        QuestionDTO questionDTO = dao.update(id, validateEntity(ctx));
        // response
        ctx.res().setStatus(200);
        ctx.json(questionDTO, Question.class);
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
    public QuestionDTO validateEntity(Context ctx) {
        return ctx.bodyValidator(QuestionDTO.class)
                .check( q -> q.getQuestion() != null && !q.getQuestion().isEmpty(), "Question must be set")
                .check( q -> q.getCorrectAnswer() != null && !q.getCorrectAnswer().isEmpty(), "Correct answer must be set")
                .check( q -> q.getCategory() != null && !q.getCategory().isEmpty(), "Category must be set")
                .check( q -> q.getDifficulty() != null && !q.getDifficulty().isEmpty(), "Difficulty must be set")
                .check( q -> q.getIncorrectAnswers() != null && !q.getIncorrectAnswers().isEmpty(), "Incorrect answers must be set")
                .get();
    }

}
