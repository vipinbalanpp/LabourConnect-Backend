package com.vipin.auth.exceptions;

public class JwtGenerationException extends RuntimeException{
    public  JwtGenerationException (String mesage){
        super(mesage);
    }
}
