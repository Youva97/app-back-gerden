package com.examen.gerden.backend.exceptions;

public class ResourceAlreadyExistException extends RuntimeException{
    public ResourceAlreadyExistException(String message) {
        super(message);
    }
}
