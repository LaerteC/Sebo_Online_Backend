package com.tcc.seboonline.repositories;

import java.util.List;

import com.tcc.seboonline.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tcc.seboonline.models.Post;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByAuthor(User user);
}
