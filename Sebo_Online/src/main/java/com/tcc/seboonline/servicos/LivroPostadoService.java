package com.tcc.seboonline.servicos;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.tcc.seboonline.controladores.LivroPostadoController;
import com.tcc.seboonline.excecoes.LivroPostadoNaoEncontradoException;
import com.tcc.seboonline.modelos.*;
import com.tcc.seboonline.repositorios.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.seboonline.excecoes.PerfilNaoEncontradoException;
import com.tcc.seboonline.excecoes.UsuarioNEncontradoException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LivroPostadoService {

	@Autowired
	private ComentariosLivrosRepository commentRepository;

	private LivroPostadoRepository postRepository;

	@Autowired
    private PerfilUsuarioRepository profileRepository;


	@Autowired
	LivroUsuarioRepository usuarioRepository;

	@Autowired
	private UsuarioService usersService;
	private final FavoritoRepository voteRepository;


	private static final Logger LOGGER = LoggerFactory.getLogger(LivroPostadoService.class);



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

		posts = postRepository.findByOcultar();


        Optional<PerfilUsuario> profile = profileRepository.findByOwner(sessionUser);

		if (profile.isEmpty())
			throw new PerfilNaoEncontradoException("O perfil relacionado a este usuário não foi encontrado");

		List<Integer> subscriptionIds = profile.get().getSubscriptionIds() != null ? profile.get().getSubscriptionIds() : new LinkedList<>();
		subscriptionIds.add(sessionUser.getId());

		Collections.sort(posts);

		return posts;
	}


	public List<LivroPostado> findLivroPostadoByGeneroLivro(String category) throws LivroPostadoNaoEncontradoException {
		List<LivroPostado> posts = new LinkedList<>();

		posts =  postRepository.findLivroPostadoByGeneroLivro(category);

		if(posts.isEmpty())
			throw new LivroPostadoNaoEncontradoException(" O gênero pesquisado não possui lvros cadastrados !");

		return posts;
	}


	public LivroPostado findById(int id) {
		LOGGER.info("Tentativa de buscar postagem com ID: {0}", id);

		Optional<LivroPostado> postagem = this.postRepository.findById(id);

		if (postagem.isPresent()) {
			LOGGER.info("Postagem encontrada com ID: {0}", id);
			return postagem.get();
		} else {
			LOGGER.info("Nenhuma postagem encontrada com ID: {0}", id);
			return null;
		}
	}


	public LivroUsuario troca(int id){
		LOGGER.info("Tentativa de buscar de um e-mail do Usuário com ID: {0}", id);
		LivroUsuario usuarioEmail = this.usuarioRepository.findEmailUser(id);

		if (usuarioEmail!= null ) {
			LOGGER.info(" A Busca pelo Usuário com o e-mail e nome do livro de SUCESSO com ID: {0}", id);
			return usuarioEmail;
		} else {
			LOGGER.info(" A Busca pelo Usuário com o e-mail e nome do livro FALSE  ID: {0}", id);
			return null;
		}
	}


}
