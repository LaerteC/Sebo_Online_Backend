package com.tcc.seboonline.models;

import com.tcc.seboonline.exceptions.VoteNotFoundException;

import java.util.Arrays;

public enum VoteType {
    UPVOTE(1), DOWNVOTE(-1),
    ;
    private int direction;

    VoteType(int direction) {
    }

    public static VoteType lookup(Integer direction) throws VoteNotFoundException {
        return Arrays.stream(VoteType.values())
                .filter(value -> value.getDirection().equals(direction))
                .findAny()
                .orElseThrow(() -> new VoteNotFoundException("Favoritar não encontrado"));
    }

    public Integer getDirection() {
        return direction;
    }
}