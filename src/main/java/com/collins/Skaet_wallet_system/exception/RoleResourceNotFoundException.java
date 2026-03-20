package com.collins.Skaet_wallet_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoleResourceNotFoundException extends RuntimeException{

    public RoleResourceNotFoundException(String message){
        super(message);
    }
}
