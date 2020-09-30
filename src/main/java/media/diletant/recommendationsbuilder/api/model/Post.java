package media.diletant.recommendationsbuilder.api.model;

import media.diletant.recommendationsbuilder.api.model.enumm.Type;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Post extends BaseEntity implements Score {
  private Type type;
  // TimePeriod timePeriod;
  // Region region;
  private String title;
  private String description;
  private String content;
  private String author;
  private int completionTimeMinutes;
  private int views;
  private Date datePublished;
  // private List<Question> questions;
  private double score;
  private boolean scoreRecalculated = false;

  public Post(
      String id,
      Type type,
      // TimePeriod timePeriod,
      // Region region,
      String title,
      String description,
      String content,
      String author,
      int completionTimeMinutes,
      int views,
      Date datePublished,
      // List<Question> questions
      double score
  ) {
    super(id);
    setType(type);
    // setTimePeriod(timePeriod);
    // setRegion(region);
    setTitle(title);
    setDescription(description);
    setContent(content);
    setAuthor(author);
    setCompletionTimeMinutes(completionTimeMinutes);
    setViews(views);
    setDatePublished(datePublished);
    setScore(score);
    // setQuestions(questions);
  }

  public Post() {
    super();
  }

  // get, set
  public double getScore() {
    if (scoreRecalculated)
      return this.score;
    var viewScore = this.views * Score.viewsCoefficient;
    var dateScore = Math.abs(new Date().getTime() - datePublished.getTime()) * Score.timeDifferenceCoefficient;
    setScore(this.score + viewScore + dateScore);
    this.scoreRecalculated = true;
    return this.score;
  }

  public void setScore(double score) {
    this.score = score;
    this.scoreRecalculated = false;
  }

  public void setDatePublished(String dateString) {
    try {
      setDatePublished(
          new SimpleDateFormat("yyyy-MM-dd").parse(dateString)
      );
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public Date getDatePublished() {
    return datePublished;
  }

  public void setDatePublished(Date datePublished) {
    this.datePublished = datePublished;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /*
  public List<Question> getQuestions() {
    return questions;
  }

  public void setQuestions(List<Question> questions) {
    if (this.type == Type.quiz
        && questions != null
        && questions.size() != 0) {
      this.questions = questions;
      this.content = questions
          .stream()
          .map(Question::getTitle)
          .collect(Collectors.joining("\n\n"));
    } else {
      this.questions = null;
    }
  }   */

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    if (this.type == Type.quiz)
      return;
    this.content = content;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  /*
  public TimePeriod getTimePeriod() {
    return this.timePeriod;
  }

  public void setTimePeriod(TimePeriod period) {
    this.timePeriod = period;
  }

  public Region getRegion() {
    return this.region;
  }

  public void setRegion(Region region) {
    this.region = region;
  }
   */

  public int getCompletionTimeMinutes() {
    return this.completionTimeMinutes;
  }

  public void setCompletionTimeMinutes(int minutes) {
    this.completionTimeMinutes = minutes;
  }

  public int getViews() {
    return this.views;
  }

  public void setViews(int views) {
    this.views = views;
  }
}
