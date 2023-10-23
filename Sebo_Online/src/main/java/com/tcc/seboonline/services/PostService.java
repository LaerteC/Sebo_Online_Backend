package com.tcc.seboonline.services;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.tcc.seboonline.models.Comment;
import com.tcc.seboonline.models.User;
import com.tcc.seboonline.repositories.CommentRepository;
import com.tcc.seboonline.repositories.PostRepository;
import com.tcc.seboonline.repositories.ProfileRepository;
import com.tcc.seboonline.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.seboonline.exceptions.ProfileNotFoundException;
import com.tcc.seboonline.exceptions.UserNotFoundException;
import com.tcc.seboonline.models.Post;
import com.tcc.seboonline.models.Profile;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

	@Autowired
	private CommentRepository commentRepository;

	private PostRepository postRepository;

	@Autowired
    private ProfileRepository profileRepository;

	@Autowired
	private UserService usersService;
	private final VoteRepository voteRepository;


	public PostService(PostRepository postRepository,
					   VoteRepository voteRepository) {
		this.postRepository = postRepository;
		this.voteRepository = voteRepository;
	}

	public List<Post> getAll() {
		return this.postRepository.findAll();
	}

	public Optional<Post> getOne(int id) { return this.postRepository.findById(id); }

	public List<Post> getAllSorted() {
		List<Post> posts = this.postRepository.findAll();
		Collections.sort(posts);
		return posts;
	}

	public Post upsert(Post post) {
		return this.postRepository.save(post);
	}

	public Comment upsertComment(Comment comment) {
		return this.commentRepository.save(comment);
	}

	public List<Post> userPosts(User user) {
		return this.postRepository.findAllByAuthor(user);
	}

	@Transactional
	public void delete(int id) {
		voteRepository.deleteByPost(postRepository.findById(id).get());
		postRepository.deleteById(id);
	}

	public List<Post> getAllSubscribedPosts(User sessionUser) throws UserNotFoundException, ProfileNotFoundException {
		List<Post> posts = new LinkedList<>();

		posts = postRepository.findAll();
        Optional<Profile> profile = profileRepository.findByOwner(sessionUser);

		if (profile.isEmpty())
			throw new ProfileNotFoundException("O perfil relacionado a este usuário não foi encontrado");

		List<Integer> subscriptionIds = profile.get().getSubscriptionIds() != null ? profile.get().getSubscriptionIds() : new LinkedList<>();
		subscriptionIds.add(sessionUser.getId());

		Collections.sort(posts);

		return posts;
	}
}
