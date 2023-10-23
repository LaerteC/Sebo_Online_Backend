package com.tcc.seboonline.dtos;

import com.tcc.seboonline.models.Profile;

public interface IBaseDTO <T> {
    T getInstanceFromProfile(Profile profile);
}
