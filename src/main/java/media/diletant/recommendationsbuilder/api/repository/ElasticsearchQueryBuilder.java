package media.diletant.recommendationsbuilder.api.repository;

public interface ElasticsearchQueryBuilder<T> {

  /**
   * Method required for searching by just one query (for example in search field on a website).
   *
   * @param searchString user query
   * @return elasticsearch-syntax json string that passed to elasticsearch as a query
   */
  String createMultiMatchQueryFor(String searchString);
}
