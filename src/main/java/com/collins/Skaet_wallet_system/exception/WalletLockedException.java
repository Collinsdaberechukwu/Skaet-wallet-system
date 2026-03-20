package com.collins.Skaet_wallet_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.LOCKED)
public class WalletLockedException extends RuntimeException{

    public WalletLockedException(String message){
        super(message);
    }
}
