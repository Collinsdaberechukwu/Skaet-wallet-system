package com.collins.Skaet_wallet_system.enums;

import com.collins.Skaet_wallet_system.exception.InvalidTransactionException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TransactionDirection {

    CREDIT("CR", "Money into wallet"),
    DEBIT("DR", "Money out of wallet");

    private final String code;
    private final String description;

    TransactionDirection(String code, String description) {
        this.code = code;
        this.description = description;
    }


    public static TransactionDirection fromCode(String code) {
        return Arrays.stream(values())
                .filter(d -> d.code.equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new InvalidTransactionException("Invalid TransactionDirection code: " + code));
    }
}
