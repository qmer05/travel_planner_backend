package dat.dtos;

import dat.entities.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class QuestionDTO {

    private Integer id;
    private String category;
    private String difficulty;
    private String question;
    private String correctAnswer;
    private List<String> incorrectAnswers;

    public QuestionDTO(Question question) {
        this.id = question.getId();
        this.category = question.getCategory();
        this.difficulty = question.getDifficulty();
        this.question = question.getQuestion();
        this.correctAnswer = question.getCorrectAnswer();
        this.incorrectAnswers = question.getIncorrectAnswers();
    }

    public QuestionDTO(String category, String difficulty, String question, String correctAnswer, List<String> incorrectAnswers) {
        this.category = category;
        this.difficulty = difficulty;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        QuestionDTO that = (QuestionDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(category, that.category) && Objects.equals(difficulty, that.difficulty) && Objects.equals(question, that.question) && Objects.equals(correctAnswer, that.correctAnswer) && Objects.equals(incorrectAnswers, that.incorrectAnswers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, difficulty, question, correctAnswer, incorrectAnswers);
    }
}
