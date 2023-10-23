package com.tcc.seboonline.repositories;

import com.tcc.seboonline.models.Post;
import com.tcc.seboonline.models.User;
import com.tcc.seboonline.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);

    long deleteByPost(Post post);
}
