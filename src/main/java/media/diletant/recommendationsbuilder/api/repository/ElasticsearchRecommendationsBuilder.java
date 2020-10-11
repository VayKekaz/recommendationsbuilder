package media.diletant.recommendationsbuilder.api.repository;

public interface ElasticsearchRecommendationsBuilder<T> {

  /**
   * Method required to search similar entities in repository.
   *
   * @param entity entity that all returned entities should be similar to
   * @return elasticsearch-syntax json string that passed to elasticsearch as a query
   */
  String buildQueryFor(T entity);
}
