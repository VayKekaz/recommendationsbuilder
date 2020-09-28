package media.diletant.recommendationsbuilder.api.model.base;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import media.diletant.recommendationsbuilder.api.model.base.enumm.Region;
import media.diletant.recommendationsbuilder.api.model.base.enumm.TimePeriod;
import media.diletant.recommendationsbuilder.api.model.base.enumm.Type;

public class Post extends BaseEntity {

  Type type;
  private String title;
  private TimePeriod timePeriod;
  private Region region;
  private int completionTimeMinutes;
  private int views;
  String content;
  List<Question> questions;

  public Post(
      String id,
      Type type,
      String title,
      TimePeriod timePeriod,
      Region region,
      int completionTimeMinutes,
      int views,
      String content,
      List<Question> questions
  ) {
    super(id);
    setType(type);
    setTitle(title);
    setTimePeriod(timePeriod);
    setRegion(region);
    setCompletionTimeMinutes(completionTimeMinutes);
    setViews(views);
    setContent(content);
    setQuestions(questions);
  }

  public Post() {
    super();
  }

  // get, set
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
  }

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
