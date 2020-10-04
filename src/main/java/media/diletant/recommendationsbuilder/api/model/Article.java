package media.diletant.recommendationsbuilder.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.Objects;

public class Article extends Post {
  public static double scoreByViews = 0.000000000000000000000000000000000001;
  public static double scoreByTimeDifference = -0.000000000000000000000000000000000001;
  private String content;
  private int views;
  private Date datePublished;
  @JsonIgnore
  private boolean scoreRecalculated = false;

  @Override
  public String buildElasticsearchString() {
    var content = Objects.requireNonNullElse(getContent(), "");
    return super.buildElasticsearchString() + " " + content;
  }

  // get, set
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public double getScore() {
    if (scoreRecalculated)
      return this.score;
    var viewScore = this.views * scoreByViews;
    var dateScore = getDateScore();
    setScore(this.score + viewScore + dateScore);
    this.scoreRecalculated = true;
    return super.getScore();
  }

  private double getDateScore() {
    double dateScore;
    try {
      dateScore = Math.abs(new Date().getTime() - datePublished.getTime()) * scoreByTimeDifference;
    } catch (NullPointerException e) {
      dateScore = 0;
    }
    return dateScore;
  }

  public Date getDatePublished() {
    return datePublished;
  }

  public void setDatePublished(Date datePublished) {
    this.datePublished = datePublished;
  }

  public int getViews() {
    return this.views;
  }

  public void setViews(int views) {
    this.views = views;
  }
}
