package media.diletant.recommendationsbuilder.api.repository;

import media.diletant.recommendationsbuilder.api.Words;
import media.diletant.recommendationsbuilder.api.model.Post;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
class RecommendationsQueryBuilder {

  public String buildQueryFor(Post post) {
    String searchString = post.buildElasticsearchString();
    return createMultiMatchQueryFor(searchString, post.getId());
  }

  private String createShouldQueryFor(String searchString) {
    var shouldQuery = new ArrayList<String>();
    for (var word : Words.history) {
      if (searchString.contains(word))
        shouldQuery.add("{" +
            "    \"match\": {" +
            "        \"content\": {" +
            "            \"query\": \"" + word + "\"," +
            "            \"boost\": 1.25" +
            "        }" +
            "    }" +
            "}"
        );
    }
    return "[" + String.join(", ", shouldQuery) + "]";
  }

  public String createMultiMatchQueryFor(String searchString, String excludeId) {
    System.out.println(searchString);
    searchString = escapeControlChars(searchString);
    System.out.println(searchString);
    // TODO: make search across quizzes
    // https://stackoverflow.com/questions/64155263/elasticsearch-search-across-multiple-fields-including-nested
    return "{" +
        "    \"query\": {" +
        "        \"bool\": {" +
        "            \"must\": [" +
        "                {" +
        "                    \"multi_match\": {" +
        "                        \"query\": \"" + searchString + "\"," +
        "                        \"type\": \"cross_fields\"," +
        "                        \"fields\": [" +
        "                            \"title^5\"," +
        "                            \"description^3\"," +
        "                            \"content\"" +
        "                        ]" +
        "                    }" +
        "                }" +
        /*
        "                {" +
        "                    \"nested\": {" +
        "                        \"path\": \"questions\"," +
        "                        \"query\": {" +
        "                            \"multi_match\": {" +
        "                                \"query\": \"" + searchString + "\"," +
        "                                \"type\": \"cross_fields\"," +
        "                                \"fields\": [" +
        "                                    \"questions.question\"," +
        "                                    \"questions.answers\"" +
        "                                ]" +
        "                            }" +
        "                        }" +
        "                    }" +
        "                }" +
         */
        "            ]," +
        "            \"should\": " + createShouldQueryFor(searchString) + "," +
        "            \"must_not\": " + createExcludeIdQuery(excludeId) +
        "        }" +
        "    }" +
        "}";
  }

  private String createExcludeIdQuery(String excludeId) {
    String mustNotQuery;
    if (excludeId == null) {
      mustNotQuery = "{" +
          "    \"ids\": {}" +
          "}";
    } else {
      mustNotQuery = "{" +
          "    \"ids\": {" +
          "        \"values\": [\"" + excludeId + "\"]" +
          "    }" +
          "}";
    }
    return mustNotQuery;
  }

  public String createMultiMatchQueryFor(String searchString) {
    var query = createMultiMatchQueryFor(searchString, null);
    System.out.println(query);
    return query;
  }

  private static String escapeControlChars(String input) {
    return input
        .replace("\n", "\\n")
        .replace("\"", "\\\"")
        .replace("\r", "\\r")
        .replace("\f", "\\f")
        .replace("\t", "\\t");
  }
}
