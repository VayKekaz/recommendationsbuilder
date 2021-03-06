package media.diletant.recommendationsbuilder.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
public class Beans {

  @Bean
  RestClient
  createRestClient() {
    final var builder = RestClient.builder(
        new HttpHost("localhost", 9200, "http"));
    builder.setDefaultHeaders(new Header[]{
        new BasicHeader("Content-Type", "application/json")});
    builder.setFailureListener(new RestClient.FailureListener() {
      @Override
      public void onFailure(Node node) {
        System.out.println("Something went wrong on request to Elasticsearch.");
        System.out.println(node.toString());
      }
    });
    return builder.build();
  }

  @Bean
  ObjectMapper
  createJacksonMapper() {
    var mapper = new ObjectMapper();
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    return mapper;
  }
}
