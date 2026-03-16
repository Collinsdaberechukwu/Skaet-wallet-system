package com.collins.Skaet_wallet_system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IdempotencyStatus {

    PROCESSING("PROCESSING"),
    SUCCESS("SUCCESS"),
    FAILED("FAILED");

    private final String idempotencyStatus;
}
