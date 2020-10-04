package media.diletant.recommendationsbuilder.api.model;

import java.util.List;

public class Question {
  private String question;
  private List<String> answers;
  private int rightAnswer;

  public Question(String question, List<String> answers, int rightAnswer) {
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

  public final List<String> getAnswers() {
    return this.answers;
  }

  public final void setAnswers(List<String> answers) {
    this.answers = answers;
  }

  public final int getRightAnswer() {
    return this.rightAnswer;
  }

  public final void setRightAnswer(int answer) {
    this.rightAnswer = answer;
  }
}
