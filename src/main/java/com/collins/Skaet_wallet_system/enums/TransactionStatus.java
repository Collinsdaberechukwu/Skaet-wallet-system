package com.collins.Skaet_wallet_system.enums;

import com.collins.Skaet_wallet_system.exception.InvalidTransactionException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TransactionStatus {

    PENDING("PND", "Transaction initiated"),
    PROCESSING("PRC", "Transaction in progress"),
    SUCCESS("SUC", "Transaction successful"),
    FAILED("FLD", "Transaction failed"),
    REVERSED("REV", "Transaction reversed"),
    CANCELLED("CNL", "Transaction cancelled");

    private final String code;
    private final String description;

    TransactionStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static TransactionStatus fromCode(String code) {
        return Arrays.stream(values())
                .filter(s -> s.code.equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new InvalidTransactionException("Invalid TransactionStatus code: " + code));
    }
}
