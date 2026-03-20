package com.collins.Skaet_wallet_system.dtos.Response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserCreationResponse {

    private Long userId;
    private String userName;
    private String email;
    private String walletNumber;

    private LocalDateTime lastLogin;

    private String roleName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
