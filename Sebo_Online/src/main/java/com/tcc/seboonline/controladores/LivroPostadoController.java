package com.tcc.seboonline.controladores;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.tcc.seboonline.DTOs.TrocaDTO;
import com.tcc.seboonline.modelos.*;
import com.tcc.seboonline.servicos.EmailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcc.seboonline.excecoes.PerfilNaoEncontradoException;
import com.tcc.seboonline.excecoes.UsuarioNEncontradoException;
import com.tcc.seboonline.utils.FiltroPalavras;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tcc.seboonline.annotations.AutorizacaoUsuario;
import com.tcc.seboonline.servicos.LivroPostadoService;
import com.tcc.seboonline.servicos.UsuarioService;

@RestController
@RequestMapping("/livropostado")
public class LivroPostadoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LivroPostadoController.class);


    private final LivroPostadoService postService;
    private final FiltroPalavras badLanguageFilter;
    private final UsuarioService userService;
    private final EmailServiceImpl emailService;

    @Autowired
    public LivroPostadoController(LivroPostadoService postService, FiltroPalavras badLanguageFilter, UsuarioService userService,EmailServiceImpl emailService) {

        this.postService = postService;
        this.badLanguageFilter = badLanguageFilter;
        this.userService = userService;
        this.emailService = emailService;

    }

    @AutorizacaoUsuario
    @GetMapping
    public ResponseEntity<List<LivroPostado>> getAllPosts() {
        LOGGER.info("Obtendo todos os posts.");
        return ResponseEntity.ok(this.postService.getAllSorted());
    }

    @AutorizacaoUsuario
    @PutMapping
    public ResponseEntity<?> upsertPost(@RequestBody LivroPostado post) throws IOException {
        if (badLanguageFilter.hasProfanity(post.getText())) {
            
            LOGGER.warn("Palavrões detectados no post: " + post.getId());
            return ResponseEntity.badRequest().body("Conteúdo impróprio");
        }
        LOGGER.info("Atualizando post: " + post.getId());
        return ResponseEntity.ok(this.postService.upsert(post));
    }


    @AutorizacaoUsuario
    @PutMapping("flag/{postId}")
    public ResponseEntity<?> updatePostVisibility(@PathVariable int postId, @RequestBody Map<String, Boolean> updateData) {
        boolean isVisible = updateData.get("isVisible");

        Optional<LivroPostado> optionalPost = Optional.ofNullable(this.postService.findById(postId));
        if (optionalPost.isPresent()) {
            LivroPostado post = optionalPost.get();
            post.setOcultar(isVisible); // Define a visibilidade do post conforme recebido

            System.out.println(" VEM QUE VALOR AQUI ??? " + post.isOcultar());


            System.out.println(" VEM QUE VALOR AQUI ??? " + post);

            postService.upsert(post); // Salva as mudanças no post

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @AutorizacaoUsuario
    @PutMapping("/comment")
    public ResponseEntity<ComentariosLivros> upsertComment(@RequestBody ComentariosLivros comment) {
        return ResponseEntity.ok(this.postService.upsertComment(comment));
    }

    @AutorizacaoUsuario
    @GetMapping("/{id}")
    public ResponseEntity<List<LivroPostado>> userPosts(@PathVariable int id) {

        try {

            return ResponseEntity.ok(this.postService.userPosts(this.userService.findById(id).get()));

        } catch (NoSuchElementException e) {

            LOGGER.error("Erro ao buscar posts do usuário com ID: " + id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @AutorizacaoUsuario
    @GetMapping("/one/{id}")
    public ResponseEntity<LivroPostado> getPost(@PathVariable int id) {
        try {
            return ResponseEntity.ok(this.postService.getOne(id).get());
        } catch (NoSuchElementException e) {
            LOGGER.error("Erro ao buscar post com ID: " + id, e);
            return ResponseEntity.badRequest().build();
        }
    }


    @AutorizacaoUsuario
    @GetMapping("category/{category}")
    public ResponseEntity<List<LivroPostado>> findLivroPostadoByGeneroLivro(@PathVariable String category) {
        try {
            return ResponseEntity.ok(this.postService.findLivroPostadoByGeneroLivro(category));

        } catch (Exception e) {
            LOGGER.error("Erro ao buscar Livros postados para a Categoria: " + category, e);
            return ResponseEntity.badRequest().build();
        }
    }


    @AutorizacaoUsuario
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable int id) {
        try {
            postService.delete(id);
            LOGGER.info("LivroPostado excluído com ID: " + id);
        } catch (NoSuchElementException e) {
            LOGGER.error("Erro ao excluir post com ID: " + id, e);
        }
    }

    @AutorizacaoUsuario
    @GetMapping("/subscribed")
    public ResponseEntity<Object> getAllSubscribedPosts(HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("user");

        try {
            return ResponseEntity.ok(this.postService.getAllSubscribedPosts(user));
        } catch (UsuarioNEncontradoException e) {
            LOGGER.error("Erro ao buscar posts de usuário inscrito.", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (PerfilNaoEncontradoException e) {
            LOGGER.error("Erro ao buscar posts de usuário inscrito.", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @AutorizacaoUsuario
    @PostMapping("troca")
    public ResponseEntity<String> troca(@RequestBody TrocaDTO trocaRequest, HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("user");

        try {
            int postId = trocaRequest.getPostId();
            int idOwner = trocaRequest.getIdOwner();

            session.setAttribute("user", user);
            LivroUsuario usuarioTroca = postService.troca(postId);
            Optional<Usuario> usuarioDono = userService.findById(idOwner);

            if (usuarioDono.isPresent()) {
                emailService.sendEmailUsuario(usuarioTroca, usuarioDono.get());
                return ResponseEntity.ok().build(); // Operação de troca e envio de email foram bem-sucedidas
            } else {
                return ResponseEntity.notFound().build(); // Usuário dono não encontrado
            }
        } catch (Exception e) {
            LOGGER.error("Erro ao Enviar e-mail !!! " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao enviar e-mail: " + e.getMessage());
        }
    }


}
