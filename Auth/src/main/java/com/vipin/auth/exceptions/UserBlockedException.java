package com.vipin.auth.exceptions;

public class UserBlockedException extends RuntimeException{
    public UserBlockedException(String message){
        super(message);
    }
}
