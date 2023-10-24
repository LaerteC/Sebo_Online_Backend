package com.tcc.seboonline.excecoes;



public class NaoLogadoException extends RuntimeException {

    public NaoLogadoException() {
    }

    public NaoLogadoException(String message) {
        super(message);
    }

    public NaoLogadoException(String message, Throwable cause) {
        super(message, cause);
    }

    public NaoLogadoException(Throwable cause) {
        super(cause);
    }

    public NaoLogadoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
