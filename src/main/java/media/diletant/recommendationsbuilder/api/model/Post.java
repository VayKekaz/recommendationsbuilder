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
})
public abstract class Post extends BaseEntity {
  private String title;
  private String description;
  private String author;
  private int completionTimeMinutes;

  public Post(
      String id,
      String title,
      String description,
      String author,
      double score
  ) {
    super(id, score);
    setTitle(title);
    setDescription(description);
    setAuthor(author);
    setCompletionTimeMinutes(completionTimeMinutes);
  }

  public Post() {
    super();
  }

  public String buildElasticsearchString() {
    var title = Objects.requireNonNullElse(getTitle(), "");
    var description = Objects.requireNonNullElse(getDescription(), "");
    return title + ' ' + description;
  }

  // get, set
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
