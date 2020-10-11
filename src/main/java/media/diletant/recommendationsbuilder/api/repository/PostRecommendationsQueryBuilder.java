package media.diletant.recommendationsbuilder.api.repository;

import media.diletant.recommendationsbuilder.api.Words;
import media.diletant.recommendationsbuilder.api.model.Post;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
class PostRecommendationsQueryBuilder implements
    ElasticsearchQueryBuilder<Post>, ElasticsearchRecommendationsBuilder<Post> {

  @Override
  public String buildQueryFor(Post post) {
    String searchString = post.buildElasticsearchString();
    return createMultiMatchQueryFor(searchString, post.getId());
  }

  public String createMultiMatchQueryFor(String searchString, String excludeId) {
    searchString = processStringQuery(searchString);
    System.out.println(searchString);
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
        this fragment was supposed to recommend not only articles but also quizzes
        but id didn't work so i just removed it
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

  private static String processStringQuery(String query) {
    return deleteControlChars(query)
        .replaceAll("\\pP", " ")
        .replaceAll(Words.stoppingPattern, " ")
        .replaceAll(Words.endingsPattern, " ");
  }

  private static String deleteControlChars(String input) {
    return input
        .replace("\n", " ")
        .replace("\"", " ")
        .replace("\r", " ")
        .replace("\f", " ")
        .replace("\t", " ");
  }

}
