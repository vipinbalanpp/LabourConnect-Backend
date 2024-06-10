package com.vipin.notification.exception;

public class InvalidEmailException extends RuntimeException{
    public InvalidEmailException(String message){
        super(message);
        System.out.println(message);

    }
}
