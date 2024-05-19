package com.example.clientes.exception;

import java.io.Serial;

public class ClienteException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;
    public ClienteException(String message, Exception e) {super(message, e);}

    public ClienteException(String message) {super(message);}
}
