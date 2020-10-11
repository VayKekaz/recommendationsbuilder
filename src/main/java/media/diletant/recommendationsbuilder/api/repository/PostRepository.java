package media.diletant.recommendationsbuilder.api.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import media.diletant.recommendationsbuilder.api.model.Post;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class PostRepository extends ElasticsearchRepository<Post> {

  @Autowired
  PostRepository(
      @Qualifier("createRestClient") RestClient restClient,
      ObjectMapper jacksonMapper,
      ElasticsearchQueryBuilder<Post> queryBuilder,
      ElasticsearchRecommendationsBuilder<Post> recommendationsBuilder
  ) {
    super(
        "post",
        restClient,
        jacksonMapper,
        queryBuilder,
        recommendationsBuilder
    );
  }
}
