package com.tcc.seboonline.modelos;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MensagensGenericas<T> {
    private String message;
    private T entity;
}
