package com.tcc.seboonline.controllers;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcc.seboonline.exceptions.ProfileNotFoundException;
import com.tcc.seboonline.exceptions.UserNotFoundException;
import com.tcc.seboonline.utils.BadLanguageFilter;
import com.tcc.seboonline.models.Comment;
import com.tcc.seboonline.models.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tcc.seboonline.annotations.Authorized;
import com.tcc.seboonline.models.Post;
import com.tcc.seboonline.services.PostService;
import com.tcc.seboonline.services.UserService;

@RestController
@RequestMapping("/post")
public class PostController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostController.class);


    private final PostService postService;
    private final BadLanguageFilter badLanguageFilter;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, BadLanguageFilter badLanguageFilter, UserService userService) {
        this.postService = postService;
        this.badLanguageFilter = badLanguageFilter;
        this.userService = userService;
    }

    @Authorized
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        LOGGER.info("Obtendo todos os posts.");
        return ResponseEntity.ok(this.postService.getAllSorted());
    }

    @Authorized
    @PutMapping
    public ResponseEntity<?> upsertPost(@RequestBody Post post) throws IOException {
        if (badLanguageFilter.hasProfanity(post.getText())) {
            
            LOGGER.warn("Palavrões detectados no post: " + post.getId());
            return ResponseEntity.badRequest().body("Conteúdo impróprio");
        }
        LOGGER.info("Atualizando post: " + post.getId());
        return ResponseEntity.ok(this.postService.upsert(post));
    }

    @Authorized
    @PutMapping("/comment")
    public ResponseEntity<Comment> upsertComment(@RequestBody Comment comment) {
        return ResponseEntity.ok(this.postService.upsertComment(comment));
    }

    @Authorized
    @GetMapping("/{id}")
    public ResponseEntity<List<Post>> userPosts(@PathVariable int id) {

        try {

            return ResponseEntity.ok(this.postService.userPosts(this.userService.findById(id).get()));

        } catch (NoSuchElementException e) {

            LOGGER.error("Erro ao buscar posts do usuário com ID: " + id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Authorized
    @GetMapping("/one/{id}")
    public ResponseEntity<Post> getPost(@PathVariable int id) {
        try {
            return ResponseEntity.ok(this.postService.getOne(id).get());
        } catch (NoSuchElementException e) {
            LOGGER.error("Erro ao buscar post com ID: " + id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Authorized
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable int id) {
        try {
            postService.delete(id);
            LOGGER.info("Post excluído com ID: " + id);
        } catch (NoSuchElementException e) {
            LOGGER.error("Erro ao excluir post com ID: " + id, e);
        }
    }

    @Authorized
    @GetMapping("/subscribed")
    public ResponseEntity<Object> getAllSubscribedPosts(HttpSession session) {
        User user = (User) session.getAttribute("user");

        try {
            return ResponseEntity.ok(this.postService.getAllSubscribedPosts(user));
        } catch (UserNotFoundException e) {
            LOGGER.error("Erro ao buscar posts de usuário inscrito.", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ProfileNotFoundException e) {
            LOGGER.error("Erro ao buscar posts de usuário inscrito.", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
