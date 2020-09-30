package media.diletant.recommendationsbuilder.api.service.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import media.diletant.recommendationsbuilder.api.model.base.Post;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class PostRepository {
  static final String indexName = "post";
  private final RestClient restClient;
  private final ObjectMapper mapper;

  @Autowired
  public PostRepository(
      @Qualifier("createRestClient") RestClient restClient,
      ObjectMapper jacksonMapper
  ) {
    this.restClient = restClient;
    this.mapper = jacksonMapper;
  }

  public Set<Post> findAll(int pageSize) throws IOException {
    return findAll(pageSize, 0);
  }

  public Set<Post> findAll(int pageSize, int startWith) throws IOException {
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
    var response = performRequestAndReturnBody(request);
    return getPostsFromFindAllResponse(response);
  }

  private String performRequestAndReturnBody(Request request) throws IOException {
    return new String(
        restClient.performRequest(request)
            .getEntity()
            .getContent()
            .readAllBytes()
    );
  }

  private Set<Post> getPostsFromFindAllResponse(String response) throws IOException {
    var responseWithoutMetadata = mapper.readTree(
        mapper.readTree(response)
            .at("/hits/hits")
            .toString()
    ); // extracting values from response string
    Set<Post> posts = new HashSet<>();
    for (var element : responseWithoutMetadata) {
      var postNode = mapper.readTree(element.toString());
      var id = postNode.at("/_id").textValue();
      var source = postNode.at("/_source");
      Post post = mapper.readValue(source.toString(), Post.class);
      post.setId(id);
      posts.add(post);
    }
    return posts;
  }

  public void save(Post entity) throws IOException {
    var endpoint = "/" + indexName + "/_doc/" + entity.getId();
    var request = new Request(
        "POST",
        endpoint);
    var jsonEntity = mapper.writeValueAsString(entity);
    request.setJsonEntity(jsonEntity);
    restClient.performRequestAsync(
        request,
        new SaveErrorHandler(entity)
    );
  }

  private class SaveErrorHandler implements ResponseListener {
    private final Post entity;

    public SaveErrorHandler(Post entity) {
      this.entity = entity;
    }

    @Override
    public void onSuccess(Response response) {
    }

    @Override
    public void onFailure(Exception exception) { // retries to save entity
      try {
        save(entity);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
