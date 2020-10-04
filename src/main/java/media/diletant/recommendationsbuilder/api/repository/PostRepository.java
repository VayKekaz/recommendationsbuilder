package media.diletant.recommendationsbuilder.api.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import media.diletant.recommendationsbuilder.api.exception.EntityNotFoundException;
import media.diletant.recommendationsbuilder.api.model.Post;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

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
@Component
public class PostRepository {
  static final String indexName = "post";
  private final RestClient restClient;
  private final ObjectMapper mapper;
  private final RecommendationsQueryBuilder recommendationsQueryBuilder;

  @Autowired
  public PostRepository(
      @Qualifier("createRestClient") RestClient restClient,
      ObjectMapper jacksonMapper,
      RecommendationsQueryBuilder recommendationsQueryBuilder
  ) {
    this.restClient = restClient;
    this.mapper = jacksonMapper;
    this.recommendationsQueryBuilder = recommendationsQueryBuilder;
  }

  public Set<Post> findAll(int pageSize) throws EntityNotFoundException {
    return findAll(pageSize, 0);
  }

  public Set<Post> findAll(int pageSize, int startWith) throws EntityNotFoundException {
    var query = "{" +
        "    \"from\": " + startWith + "," +
        "    \"size\": " + pageSize + "," +
        "    \"query\": {" +
        "        \"match_all\": {}" +
        "    }" +
        "}";
    System.out.println(query);
    var request = new Request("GET", "/" + indexName + "/_search");
    request.setJsonEntity(query);
    var response = performRequestAndGetBody(request);
    return getPostsFromSearchResponse(response);
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

  private Set<Post> getPostsFromSearchResponse(String response) {
    Set<Post> posts;
    try {
      var responseWithoutMetadata = mapper.readTree(
          mapper.readTree(response)
              .at("/hits/hits")
              .toString()
      ); // extracting values from response string
      posts = new HashSet<>();
      for (var element : responseWithoutMetadata) {
        var postNode = mapper.readTree(element.toString());
        var post = getPostFrom(postNode);
        posts.add(post);
      }
    } catch (IOException e) {
      e.printStackTrace();
      posts = null;
    }
    return posts;
  }

  private Post getPostFrom(JsonNode postNode) {
    var id = postNode.at("/_id").textValue();
    var score = postNode.at("/score").asDouble();
    var source = postNode.at("/_source"); // actual entity
    Post post;
    try {
      post = mapper.readValue(source.toString(), Post.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("Invalid postNode provided");
    }
    post.setId(id);
    post.setScore(score);
    return post;
  }

  public Post getBy(String id) throws EntityNotFoundException {
    var request = new Request("GET", "/" + indexName + "/_doc/" + id);
    String response;
    try {
      response = performRequestAndGetBody(request);
      if (entityFoundWithin(response))
        return getPostFrom(mapper.readTree(response));
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

  public List<Post> searchBy(String query) throws EntityNotFoundException {
    var request = new Request("GET", "/" + indexName + "/_search");
    request.setJsonEntity(recommendationsQueryBuilder.createMultiMatchQueryFor(query));
    var posts = getPostsFromSearchResponse(performRequestAndGetBody(request));
    return posts.stream()
        .sorted(Comparator.comparingDouble(Post::getScore))
        .collect(Collectors.toUnmodifiableList());
  }

  public List<Post> getSimilarById(String id) throws EntityNotFoundException {
    return getSimilarTo(getBy(id));
  }

  public List<Post> getSimilarTo(Post post) throws EntityNotFoundException {
    var request = new Request("GET", "/" + indexName + "/_search");
    request.setJsonEntity(recommendationsQueryBuilder.buildQueryFor(post));
    var posts = getPostsFromSearchResponse(performRequestAndGetBody(request));
    return posts.stream()
        .sorted(Comparator.comparingDouble(Post::getScore))
        .collect(Collectors.toUnmodifiableList());
  }
}
