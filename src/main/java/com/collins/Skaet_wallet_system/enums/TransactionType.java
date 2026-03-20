package com.collins.Skaet_wallet_system.enums;

import jakarta.transaction.InvalidTransactionException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TransactionType {

    FUNDING("FUND", "Wallet funding"),
    WITHDRAWAL("WDR", "Wallet withdrawal"),
    TRANSFER("TRF", "Wallet to wallet transfer"),
    PAYMENT("PMT", "Payment for goods/services"),
    REFUND("RFD", "Refund transaction");

    private final String code;
    private final String description;

    TransactionType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static TransactionType fromCode(String code) throws InvalidTransactionException {
        return Arrays.stream(values())
                .filter(t -> t.code.equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new InvalidTransactionException("Invalid TransactionType code: " + code));
    }
}
