package com.tcc.seboonline.modelos;

import com.tcc.seboonline.excecoes.FavoritoNaoEncontradoException;

import java.util.Arrays;

public enum TipoFavorito {
    UPVOTE(1), DOWNVOTE(-1),
    ;
    private int direction;

    TipoFavorito(int direction) {
    }

    public static TipoFavorito lookup(Integer direction) throws FavoritoNaoEncontradoException {
        return Arrays.stream(TipoFavorito.values())
                .filter(value -> value.getDirection().equals(direction))
                .findAny()
                .orElseThrow(() -> new FavoritoNaoEncontradoException("Favoritar não encontrado"));
    }

    public Integer getDirection() {
        return direction;
    }
}