package com.tcc.seboonline.DTOs;

import com.tcc.seboonline.modelos.PerfilUsuario;

public interface IBaseDTO <T> {
    T getInstanceFromProfile(PerfilUsuario profile);
}
