package media.diletant.recommendationsbuilder.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import media.diletant.recommendationsbuilder.api.model.Article;
import media.diletant.recommendationsbuilder.api.model.Post;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class ObjectMapperTest {
  private final ObjectMapper mapper = new Beans().createJacksonMapper();

  @Test
  void deserializationTest() throws JsonProcessingException, ParseException {
    var testDate = "2020-12-25";
    var df = new SimpleDateFormat("yyyy-MM-dd");
    var post = new Article();
    post.setDatePublished(df.parse(testDate));
    var deserializedValue = mapper.writeValueAsString(post);
    var serializedDate =
        mapper.readValue(deserializedValue, Map.class).get("datePublished");
    System.out.println("serializedDate=" + serializedDate.toString() + "\ntestDate=" + testDate);
    assert serializedDate.toString().equals(testDate);
  }

  @Test
  void polymorphicMappingTest() throws JsonProcessingException {
    var article = new Article();
    article.setContent("Some content yay");

    var articleJson = mapper.writeValueAsString(article);
    System.out.println(articleJson);

    var articleFromJson = mapper.readValue(articleJson, Post.class);

    System.out.println("articleFromJson type is " + articleFromJson.getClass().getSimpleName());
    assert articleFromJson instanceof Article;
  }
}
