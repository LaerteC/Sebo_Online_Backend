package com.tcc.seboonline.controllers;

import com.tcc.seboonline.annotations.Authorized;
import com.tcc.seboonline.exceptions.PostNotFoundException;
import com.tcc.seboonline.exceptions.VoteNotFoundException;
import com.tcc.seboonline.models.Vote;
import com.tcc.seboonline.services.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/vote")
@AllArgsConstructor
public class VoteController {

    private final VoteService voteService;



    @Authorized
    @PostMapping
    public ResponseEntity<Void> vote(@RequestBody Vote vote) throws PostNotFoundException, VoteNotFoundException {
        voteService.vote(vote);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/one/{userId}&{postId}")
    public ResponseEntity<Optional<Vote>> getVoteByUserAndPost(@PathVariable int userId, @PathVariable int postId){
        return ResponseEntity.ok(this.voteService.getVoteByUserIdAndPostId(userId, postId));
    }
}
