package dat.config;


import dat.entities.Question;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class Populate {
    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Question q1 = new Question("geography", "hard",
                    "Brno is a city in which country?",
                    "Czech Republic",
                    List.of("Switzerland", "Norway", "Netherlands"));
            Question q2 = new Question("geography", "easy",
                    "What is the capital of France?",
                    "Paris",
                    List.of("London", "Berlin", "Madrid"));
            Question q3 = new Question("geography", "medium",
                    "What is the capital of Spain?",
                    "Madrid",
                    List.of("Barcelona", "Valencia", "Seville"));
            em.persist(q1);
            em.persist(q2);
            em.persist(q3);
            em.getTransaction().commit();
        }
    }
}
