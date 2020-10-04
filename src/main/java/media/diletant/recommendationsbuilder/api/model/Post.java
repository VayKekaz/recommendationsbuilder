package media.diletant.recommendationsbuilder.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Article.class, name = "article"),
    @JsonSubTypes.Type(value = Newspaper.class, name = "newspaper"),
    @JsonSubTypes.Type(value = Quiz.class, name = "quiz")
})
public abstract class Post extends BaseEntity {
  private String title;
  private String description;
  private String author;
  private int completionTimeMinutes;
  double score;

  public Post(
      String id,
      String title,
      String description,
      String author,
      double score
  ) {
    super(id);
    setTitle(title);
    setDescription(description);
    setAuthor(author);
    setCompletionTimeMinutes(completionTimeMinutes);
    setScore(score);
  }

  public Post() {
    super();
  }

  @JsonIgnore
  public String buildElasticsearchString() {
    var title = Objects.requireNonNullElse(getTitle(), "");
    var description = Objects.requireNonNullElse(getDescription(), "");
    return title + ' ' + description;
  }

  // get, set
  public double getScore() {
    return this.score;
  }

  public void setScore(double score) {
    this.score = score;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getCompletionTimeMinutes() {
    return this.completionTimeMinutes;
  }

  public void setCompletionTimeMinutes(int minutes) {
    this.completionTimeMinutes = minutes;
  }

}
