package media.diletant.recommendationsbuilder.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import media.diletant.recommendationsbuilder.api.model.base.Post;
import media.diletant.recommendationsbuilder.api.service.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;


@Controller
@RequestMapping("/api/post/")
public class PostController {

  private final PostRepository postRepository;

  @Autowired
  PostController(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @GetMapping("/")
  public ResponseEntity<Iterable<Post>> getAllPosts(@RequestParam("pageSize") int pageSize) throws IOException {
    if (pageSize == 0) pageSize = 10;
    return new ResponseEntity<>(
        postRepository.findAll(pageSize),
        HttpStatus.OK
    );
  }
}
