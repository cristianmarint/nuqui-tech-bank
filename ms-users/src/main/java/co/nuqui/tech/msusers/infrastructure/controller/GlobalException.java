package co.nuqui.tech.msusers.infrastructure.controller;

public class GlobalException extends RuntimeException {

    public GlobalException(String message) {
        super(message);
    }
}