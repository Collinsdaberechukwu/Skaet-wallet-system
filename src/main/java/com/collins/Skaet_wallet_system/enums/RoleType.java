package com.collins.Skaet_wallet_system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {

    ADMIN("ADMIN_USER"),
    USER("USER");

    private final String roleType;
}
