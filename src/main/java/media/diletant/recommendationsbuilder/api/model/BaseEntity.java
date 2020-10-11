package media.diletant.recommendationsbuilder.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Post.class, name = "post"),
})
public abstract class BaseEntity {
  private String id;
  private double score;

  public BaseEntity() {
  }

  public BaseEntity(String id, double score) {
    this.id = id;
    this.score = score;
  }

  // get, set
  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public double getScore() {
    return score;
  }

  public void setScore(double score) {
    this.score = score;
  }
}
