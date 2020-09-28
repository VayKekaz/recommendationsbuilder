package media.diletant.recommendationsbuilder.api.model.base;

import java.util.Set;

public final class Question extends BaseEntity {
  private String question;
  private Set<String> answers;
  private int rightAnswer;

  public Question(String id, String question, Set<String> answers, int rightAnswer) {
    super(id);
    this.question = question;
    this.answers = answers;
    this.rightAnswer = rightAnswer;
  }

  public Question() {
  }

  // get, set
  public final boolean isRightAnswer(int answer) {
    return this.rightAnswer == answer;
  }

  public final String getTitle() {
    return this.question;
  }

  public final void setQuestion(String question) {
    this.question = question;
  }

  public final Set<String> getAnswers() {
    return this.answers;
  }

  public final void setAnswers(Set<String> answers) {
    this.answers = answers;
  }

  public final int getRightAnswer() {
    return this.rightAnswer;
  }

  public final void setRightAnswer(int answer) {
    this.rightAnswer = answer;
  }
}
