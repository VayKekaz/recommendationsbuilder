package media.diletant.recommendationsbuilder.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import media.diletant.recommendationsbuilder.api.model.base.Post;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ObjectMapperTest {
  private final ObjectMapper mapper = new Beans().createJacksonMapper();

  @Test
  void deserializationTest() throws JsonProcessingException {
    var testDate = "2020-12-25";
    var post = new Post();
    post.setDatePublished(testDate);
    var deserializedValue = mapper.writeValueAsString(post);
    var serializedDate = mapper.readValue(deserializedValue, Map.class).get("datePublished");
    System.out.println("serializedDate=" + serializedDate.toString() + "\ntestDate=" + testDate);
    assert serializedDate.toString().equals(testDate);
  }

  @Test
  void dateTimeFormatTest() throws ParseException {
    var testDate = "2020-12-25";
    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(testDate);
    System.out.println(date);
    assert date.toString().equals(testDate);
  }
}
