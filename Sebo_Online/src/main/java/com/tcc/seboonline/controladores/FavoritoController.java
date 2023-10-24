package com.tcc.seboonline.controladores;

import com.tcc.seboonline.annotations.AutorizacaoUsuario;
import com.tcc.seboonline.excecoes.LivroPostadoNaoEncontradoException;
import com.tcc.seboonline.excecoes.FavoritoNaoEncontradoException;
import com.tcc.seboonline.modelos.Favoritos;
import com.tcc.seboonline.servicos.FavoritoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/vote")
@AllArgsConstructor
public class FavoritoController {

    private final FavoritoService voteService;



    @AutorizacaoUsuario
    @PostMapping
    public ResponseEntity<Void> vote(@RequestBody Favoritos vote) throws LivroPostadoNaoEncontradoException, FavoritoNaoEncontradoException {
        voteService.vote(vote);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/one/{userId}&{postId}")
    public ResponseEntity<Optional<Favoritos>> getVoteByUserAndPost(@PathVariable int userId, @PathVariable int postId){
        return ResponseEntity.ok(this.voteService.getVoteByUserIdAndPostId(userId, postId));
    }
}
