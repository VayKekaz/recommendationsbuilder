package media.diletant.recommendationsbuilder.api.model;

import java.util.UUID;

public abstract class BaseEntity {
  private String id;

  public BaseEntity() {
  }

  public BaseEntity(String id) {
    this.id = id;
  }

  // get, set
  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
