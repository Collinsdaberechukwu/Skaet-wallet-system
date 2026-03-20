package com.collins.Skaet_wallet_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTransactionException extends RuntimeException {

    public InvalidTransactionException(String msg){
        super(msg);
    }
}
