package com.project.ecommercebackend.exception;

public class APIException extends RuntimeException{
    public APIException(String message) {
        super(message);
    }
    public APIException() {
    }

}
