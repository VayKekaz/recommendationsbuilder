package media.diletant.recommendationsbuilder.api.controller;

import media.diletant.recommendationsbuilder.api.Words;
import media.diletant.recommendationsbuilder.api.exception.EntityNotFoundException;
import media.diletant.recommendationsbuilder.api.model.Post;
import media.diletant.recommendationsbuilder.api.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
  public ResponseEntity<Iterable<Post>> getAllPosts(
      @RequestParam Optional<Integer> pageSize) throws IOException {
    return new ResponseEntity<>(
        postRepository.findAll(pageSize.orElse(10)),
        HttpStatus.OK
    );
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
    System.out.println("METHOD CALLED");
    return response;
  }

  @GetMapping("/test")
  public ResponseEntity<String[]> test() {
    return new ResponseEntity<>(
        Words.stopping,
        HttpStatus.OK
    );
  }
}
