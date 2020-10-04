package media.diletant.recommendationsbuilder.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Quiz extends Post {
  private int questionCount;
  private List<Question> questions;

  public Quiz(
      String id,
      String title,
      String description,
      String author,
      double score,
      int questionCount,
      List<Question> questions
  ) {
    super(
        id,
        title,
        description,
        author,
        score
    );
    setQuestionCount(questionCount);
    setQuestions(questions);
  }

  public Quiz() {
    super();
  }

  @Override
  public String buildElasticsearchString() {
    return super.buildElasticsearchString() + " " +
        getQuestions().stream().map(question ->
            question.getTitle() + " " + String.join(" ", question.getAnswers())
        ).collect(Collectors.joining(", "));
  }

  // get, set
  public int getQuestionCount() {
    return questionCount;
  }

  public void setQuestionCount(int questionCount) {
    this.questionCount = questionCount;
  }

  public List<Question> getQuestions() {
    List<Question> questions;
    questions = Objects.requireNonNullElseGet(this.questions, ArrayList::new);
    return questions;
  }

  public void setQuestions(List<Question> questions) {
    this.questions = questions;
    setQuestionCount(questions.size());
  }
}
