package media.diletant.recommendationsbuilder.api.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import media.diletant.recommendationsbuilder.api.Words;
import media.diletant.recommendationsbuilder.api.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@Component
class RecommendationsQueryBuilder {
  private final ObjectMapper mapper;

  @Autowired
  public RecommendationsQueryBuilder(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  public String buildQueryFor(Post post) {
    var searchString = escapeControlChars(
        post.getTitle() + " " +
            post.getDescription() + " " +
            post.getContent());
    var should_query = new ArrayList<String>();

    for (var word : Words.history) {
      if (searchString.contains(word))
        should_query.add("{" +
            "    \"match\": {" +
            "        \"content\": {" +
            "            \"query\": \"" + word + "\"," +
            "            \"boost\": 1.25" +
            "        }" +
            "    }" +
            "}"
        );
    }

    return "{" +
        "    \"query\": {" +
        "        \"bool\": {" +
        "            \"must\": {" +
        "                \"multi_match\": {" +
        "                    \"query\": \"" + searchString + "\"," +
        "                    \"fields\": [\"title^5\", \"content^2\"]," +
        "                    \"slop\": 1" +
        "                }" +
        "            }," +
        "            \"should\": [" +
        "            " + String.join(", ", should_query) +
        "            ]" +
        "        }" +
        "    }" +
        "}";
  }

  private String escapeControlChars(String input) {
    return input
        .replace("\n", "\\n")
        .replace("\"", "\\\"")
        .replace("\r", "\\r")
        .replace("\f", "\\f")
        .replace("\t", "\\t");
  }
}
