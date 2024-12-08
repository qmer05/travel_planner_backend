package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.QuestionDTO;
import dat.entities.Question;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class QuestionDAO implements IDAO<QuestionDTO, Integer> {

    private static QuestionDAO instance;
    private static EntityManagerFactory emf;

    public static QuestionDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new QuestionDAO();
        }
        return instance;
    }

    @Override
    public QuestionDTO read(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Question question = em.find(Question.class, integer);
            return new QuestionDTO(question);
        }
    }

    @Override
    public List<QuestionDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<QuestionDTO> query = em.createQuery("SELECT new dat.dtos.QuestionDTO(q) FROM Question q", QuestionDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public QuestionDTO create(QuestionDTO questionDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Question question = new Question(questionDTO);
            em.persist(question);
            em.getTransaction().commit();
            return new QuestionDTO(question);
        }
    }

    @Override
    public QuestionDTO update(Integer integer, QuestionDTO questionDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Question q = em.find(Question.class, integer);
            q.setQuestion(questionDTO.getQuestion());
            q.setCategory(questionDTO.getCategory());
            q.setDifficulty(questionDTO.getDifficulty());
            q.setCorrectAnswer(questionDTO.getCorrectAnswer());
            q.setIncorrectAnswers(questionDTO.getIncorrectAnswers());
            Question mergedQuestion = em.merge(q);
            em.getTransaction().commit();
            return mergedQuestion != null ? new QuestionDTO(mergedQuestion) : null;
        }
    }

    @Override
    public void delete(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Question question = em.find(Question.class, integer);
            if (question != null) {
                em.remove(question);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Question question = em.find(Question.class, integer);
            return question != null;
        }
    }
}
