package media.diletant.recommendationsbuilder.api.controller;

import media.diletant.recommendationsbuilder.api.exception.EntityNotFoundException;
import media.diletant.recommendationsbuilder.api.model.Post;
import media.diletant.recommendationsbuilder.api.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/post")
public class PostController {

  private final PostRepository postRepository;

  @Autowired
  PostController(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @GetMapping
  public ResponseEntity<Iterable<Post>> getAllPosts(@RequestParam Optional<Integer> pageSize) {
    ResponseEntity<Iterable<Post>> response;
    try {
      response = new ResponseEntity<>(
          postRepository.findAll(pageSize.orElse(10)),
          HttpStatus.OK
      );
    } catch (EntityNotFoundException e) {
      response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return response;
  }

  @GetMapping(value = "/search", consumes = "application/json")
  public ResponseEntity<Iterable<Post>> searchFor(@RequestBody Map<String, Object> body) {
    ResponseEntity<Iterable<Post>> response;
    try {
      var query = (String) body.getOrDefault("query", "");
      response = new ResponseEntity<>(
          postRepository.searchBy(query),
          HttpStatus.OK
      );
    } catch (ClassCastException e) {
      response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (EntityNotFoundException e) {
      e.printStackTrace();
      response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return response;
  }

  @GetMapping("/recommendations/{postId}")
  public ResponseEntity<Iterable<Post>> getRecommendationsFor(@PathVariable String postId) {
    ResponseEntity<Iterable<Post>> response;
    try {
      response = new ResponseEntity<>(
          postRepository.getSimilarById(postId),
          HttpStatus.OK
      );
    } catch (EntityNotFoundException e) {
      response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return response;
  }
}
