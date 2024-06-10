package com.vipin.auth.exceptions;

import javax.naming.AuthenticationException;

public class UserAuthenticationException extends RuntimeException {
    public UserAuthenticationException(String message){
        super(message);
    }

}
