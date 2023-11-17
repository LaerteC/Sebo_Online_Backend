package com.tcc.seboonline.modelos;

import com.tcc.seboonline.excecoes.FavoritoNaoEncontradoException;

import java.util.Arrays;

public enum EnumTipoFavorito {
    favoritar(1), desfavoritar(-1),
    ;
    private int direction;

    EnumTipoFavorito(int direction) {
    }

    public static EnumTipoFavorito lookup(Integer direction) throws FavoritoNaoEncontradoException {
        return Arrays.stream(EnumTipoFavorito.values())
                .filter(value -> value.getDirection().equals(direction))
                .findAny()
                .orElseThrow(() -> new FavoritoNaoEncontradoException("Favoritar não encontrado"));
    }

    public Integer getDirection() {
        return direction;
    }
}