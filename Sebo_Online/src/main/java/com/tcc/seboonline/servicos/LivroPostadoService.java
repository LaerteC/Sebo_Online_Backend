package com.tcc.seboonline.servicos;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.tcc.seboonline.modelos.ComentariosLivros;
import com.tcc.seboonline.modelos.LivroPostado;
import com.tcc.seboonline.modelos.Usuario;
import com.tcc.seboonline.repositorios.ComentariosLivrosRepository;
import com.tcc.seboonline.repositorios.LivroPostadoRepository;
import com.tcc.seboonline.repositorios.PerfilUsuarioRepository;
import com.tcc.seboonline.repositorios.FavoritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.seboonline.excecoes.PerfilNaoEncontradoException;
import com.tcc.seboonline.excecoes.UsuarioNEncontradoException;
import com.tcc.seboonline.modelos.PerfilUsuario;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LivroPostadoService {

	@Autowired
	private ComentariosLivrosRepository commentRepository;

	private LivroPostadoRepository postRepository;

	@Autowired
    private PerfilUsuarioRepository profileRepository;

	@Autowired
	private UsuarioService usersService;
	private final FavoritoRepository voteRepository;


	public LivroPostadoService(LivroPostadoRepository postRepository,
							   FavoritoRepository voteRepository) {
		this.postRepository = postRepository;
		this.voteRepository = voteRepository;
	}

	public List<LivroPostado> getAll() {
		return this.postRepository.findAll();
	}

	public Optional<LivroPostado> getOne(int id) { return this.postRepository.findById(id); }

	public List<LivroPostado> getAllSorted() {
		List<LivroPostado> posts = this.postRepository.findAll();
		Collections.sort(posts);
		return posts;
	}

	public LivroPostado upsert(LivroPostado post) {
		return this.postRepository.save(post);
	}

	public ComentariosLivros upsertComment(ComentariosLivros comment) {
		return this.commentRepository.save(comment);
	}

	public List<LivroPostado> userPosts(Usuario user) {
		return this.postRepository.findAllByAuthor(user);
	}

	@Transactional
	public void delete(int id) {
		voteRepository.deleteByPost(postRepository.findById(id).get());
		postRepository.deleteById(id);
	}

	public List<LivroPostado> getAllSubscribedPosts(Usuario sessionUser) throws UsuarioNEncontradoException, PerfilNaoEncontradoException {
		List<LivroPostado> posts = new LinkedList<>();

		posts = postRepository.findAll();
        Optional<PerfilUsuario> profile = profileRepository.findByOwner(sessionUser);

		if (profile.isEmpty())
			throw new PerfilNaoEncontradoException("O perfil relacionado a este usuário não foi encontrado");

		List<Integer> subscriptionIds = profile.get().getSubscriptionIds() != null ? profile.get().getSubscriptionIds() : new LinkedList<>();
		subscriptionIds.add(sessionUser.getId());

		Collections.sort(posts);

		return posts;
	}
}
