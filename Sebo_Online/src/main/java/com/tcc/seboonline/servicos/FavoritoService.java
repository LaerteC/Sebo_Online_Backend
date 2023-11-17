package com.tcc.seboonline.servicos;

import com.tcc.seboonline.excecoes.LivroPostadoNaoEncontradoException;
import com.tcc.seboonline.modelos.LivroPostado;
import com.tcc.seboonline.modelos.Usuario;
import com.tcc.seboonline.modelos.Favoritos;
import com.tcc.seboonline.repositorios.LivroPostadoRepository;
import com.tcc.seboonline.repositorios.UsuarioRepository;
import com.tcc.seboonline.repositorios.FavoritoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.tcc.seboonline.modelos.EnumTipoFavorito.desfavoritar;
import static com.tcc.seboonline.modelos.EnumTipoFavorito.favoritar;


@Service
@AllArgsConstructor
public class FavoritoService {


    private final FavoritoRepository voteRepository;
    private final LivroPostadoRepository postRepository;
    private final UsuarioRepository userRepository;

    @Transactional
    public Optional<Favoritos> getVoteByUserIdAndPostId(int userId, int postId) {
        Usuario user = userRepository.findById(userId).get();
        LivroPostado post = postRepository.findById(postId).get();
        Optional<Favoritos> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, user);
        return voteByPostAndUser;
    }


    @Transactional
    public void vote(Favoritos vote) throws LivroPostadoNaoEncontradoException {
        LivroPostado post = postRepository.findById(vote.getPost().getId())
                .orElseThrow(() -> new LivroPostadoNaoEncontradoException("Postagem não encontrada com ID - " + vote.getPost().getId()));

        Optional<Favoritos> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, vote.getUser());


        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(vote.getVoteType())) {
            if (voteByPostAndUser.get().getVoteType().equals(favoritar)){
                voteRepository.deleteById(voteByPostAndUser.get().getVoteId());
                post.setVoteCount(post.getVoteCount() - 1);
                postRepository.save(post);
                return;
            } else {
                voteRepository.deleteById(voteByPostAndUser.get().getVoteId());
                post.setVoteCount(post.getVoteCount() + 1);
                postRepository.save(post);
                return;
            }
        }

        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(favoritar) && vote.getVoteType().equals(desfavoritar))  {
            post.setVoteCount(post.getVoteCount() - 2);
            voteRepository.deleteById(voteByPostAndUser.get().getVoteId());
        }

        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(desfavoritar) && vote.getVoteType().equals(favoritar))  {
            post.setVoteCount(post.getVoteCount() + 2);
            voteRepository.deleteById(voteByPostAndUser.get().getVoteId());
        }

        if (!voteByPostAndUser.isPresent()) {
            if (favoritar.equals(vote.getVoteType())) {
                post.setVoteCount(post.getVoteCount() + 1);
            } else {
                post.setVoteCount(post.getVoteCount() - 1);
            }
        }

        voteRepository.save(mapToVote(vote, post));
        postRepository.save(post);
    }

    private Favoritos mapToVote(Favoritos vote, LivroPostado post) {
        return Favoritos.builder()
                .voteType(vote.getVoteType())
                .post(post)
                .user(vote.getUser())
                .build();
    }

}
