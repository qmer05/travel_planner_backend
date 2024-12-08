package dat.entities;

import dat.dtos.QuestionDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Setter
    @Column(name = "category", nullable = false)
    private String category;

    @Setter
    @Column(name = "difficulty", nullable = false, unique = true)
    private String difficulty;

    @Setter
    @Column(name = "question", nullable = false, unique = true)
    private String question;

    @Setter
    @Column(name = "correct_answer", nullable = false, unique = true)
    private String correctAnswer;

    @Setter
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "incorrect_answers", nullable = false, unique = true)
    private List<String> incorrectAnswers;

    public Question(String category, String difficulty, String question, String correctAnswer, List<String> incorrectAnswers) {
        this.category = category;
        this.difficulty = difficulty;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
    }

    public Question(QuestionDTO questionDTO) {
        this.id = questionDTO.getId();
        this.category = questionDTO.getCategory();
        this.question = questionDTO.getQuestion();
        this.correctAnswer = questionDTO.getCorrectAnswer();
        this.incorrectAnswers = questionDTO.getIncorrectAnswers();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id) && Objects.equals(category, question.category) && Objects.equals(difficulty, question.difficulty) && Objects.equals(question, question.question) && Objects.equals(correctAnswer, question.correctAnswer) && Objects.equals(incorrectAnswers, question.incorrectAnswers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, difficulty, question, correctAnswer, incorrectAnswers);
    }
}
