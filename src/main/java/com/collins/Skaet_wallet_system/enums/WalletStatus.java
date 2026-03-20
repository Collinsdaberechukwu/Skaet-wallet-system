package com.collins.Skaet_wallet_system.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum WalletStatus {

    ACTIVE("ACT", "Wallet is active"),
    INACTIVE("INA", "Wallet is inactive"),
    SUSPENDED("SUS", "Wallet temporarily restricted"),
    BLOCKED("BLK", "Wallet blocked due to risk/fraud"),
    CLOSED("CLS", "Wallet permanently closed");

    private final String code;
    private final String description;

    WalletStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static WalletStatus fromCode(String code) {
        return Arrays.stream(values())
                .filter(w -> w.code.equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid WalletStatus code: " + code));
    }
}
