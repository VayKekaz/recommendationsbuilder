package media.diletant.recommendationsbuilder.api.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import media.diletant.recommendationsbuilder.api.exception.EntityNotFoundException;
import media.diletant.recommendationsbuilder.api.model.BaseEntity;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class powered by RestClient.
 * Makes requests to elasticsearch and handles serialization / deserialization.
 */
public abstract class ElasticsearchRepository<T extends BaseEntity> {
  private final String indexName;
  private final RestClient restClient;
  private final ObjectMapper mapper;
  private final ElasticsearchQueryBuilder<T> queryBuilder;
  private final ElasticsearchRecommendationsBuilder<T> recommendationsBuilder;

  public ElasticsearchRepository(
      String indexName,
      RestClient restClient,
      ObjectMapper jacksonMapper,
      ElasticsearchQueryBuilder<T> queryBuilder,
      ElasticsearchRecommendationsBuilder<T> recommendationsBuilder
  ) {
    this.indexName = indexName;
    this.restClient = restClient;
    this.mapper = jacksonMapper;
    this.queryBuilder = queryBuilder;
    this.recommendationsBuilder = recommendationsBuilder;
  }

  public Set<T> findAll(int pageSize) throws EntityNotFoundException {
    return findAll(pageSize, 0);
  }

  public Set<T> findAll(int pageSize, int startWith) throws EntityNotFoundException {
    var query = "{" +
        "    \"from\": " + startWith + "," +
        "    \"size\": " + pageSize + "," +
        "    \"query\": {" +
        "        \"match_all\": {}" +
        "    }" +
        "}";
    var request = new Request("GET", "/" + indexName + "/_search");
    request.setJsonEntity(query);
    var response = performRequestAndGetBody(request);
    return getEntitiesFromSearchResponse(response);
  }

  private String performRequestAndGetBody(Request request) throws EntityNotFoundException {
    String body;
    try {
      body = new String(
          restClient.performRequest(request)
              .getEntity()
              .getContent()
              .readAllBytes()
      );
    } catch (ResponseException e) {
      e.printStackTrace();
      throw new EntityNotFoundException();
    } catch (IOException e) { // just a placeholder, this should not be thrown
      e.printStackTrace();
      body = null;
    }
    return body;
  }

  private Set<T> getEntitiesFromSearchResponse(String response) {
    Set<T> entities;
    try {
      var responseWithoutMetadata = mapper.readTree(
          mapper.readTree(response)
              .at("/hits/hits")
              .toString()
      ); // extracting values from response string
      entities = new HashSet<>();
      for (var element : responseWithoutMetadata) {
        var entityNode = mapper.readTree(element.toString());
        var entity = getEntitiesFrom(entityNode);
        entities.add(entity);
      }
    } catch (IOException e) {
      e.printStackTrace();
      entities = null;
    }
    return entities;
  }

  private T getEntitiesFrom(JsonNode entityNode) {
    var id = entityNode.at("/_id").textValue();
    var score = entityNode.at("/score").asDouble();
    var source = entityNode.at("/_source"); // actual entity
    T entity;
    try {
      entity = mapper.readValue(
          source.toString(),
          new TypeReference<>() {
          }
      );
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("Invalid entityNode provided");
    }
    entity.setId(id);
    entity.setScore(score);
    return entity;
  }

  public T getBy(String id) throws EntityNotFoundException {
    var request = new Request("GET", "/" + indexName + "/_doc/" + id);
    String response;
    try {
      response = performRequestAndGetBody(request);
      if (entityFoundWithin(response))
        return getEntitiesFrom(mapper.readTree(response));
      else
        throw new EntityNotFoundException();
    } catch (IOException e) {
      throw new EntityNotFoundException();
    }
  }

  private boolean entityFoundWithin(String response) {
    boolean found;
    try {
      var foundNode = mapper.readTree(response).at("/found");
      var reader = mapper.readerFor(boolean.class);
      found = reader.readValue(foundNode);
    } catch (IOException e) {
      found = false;
    }
    return found;
  }

  public List<T> searchBy(String query) throws EntityNotFoundException {
    var request = new Request("GET", "/" + indexName + "/_search");
    request.setJsonEntity(queryBuilder.createMultiMatchQueryFor(query));
    var entities = getEntitiesFromSearchResponse(performRequestAndGetBody(request));
    return entities.stream()
        .sorted(Comparator.comparingDouble(BaseEntity::getScore))
        .collect(Collectors.toUnmodifiableList());
  }

  public List<T> getSimilarById(String id) throws EntityNotFoundException {
    return getSimilarTo(getBy(id));
  }

  public List<T> getSimilarTo(T entity) throws EntityNotFoundException {
    var request = new Request("GET", "/" + indexName + "/_search");
    request.setJsonEntity(recommendationsBuilder.buildQueryFor(entity));
    var entities = getEntitiesFromSearchResponse(performRequestAndGetBody(request));
    return entities.stream()
        .sorted(Comparator.comparingDouble(BaseEntity::getScore))
        .collect(Collectors.toUnmodifiableList());
  }
}
