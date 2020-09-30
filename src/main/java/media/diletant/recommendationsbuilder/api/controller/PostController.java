package media.diletant.recommendationsbuilder.api.controller;

import media.diletant.recommendationsbuilder.api.model.base.Post;
import media.diletant.recommendationsbuilder.api.service.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;


@Controller
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
}
