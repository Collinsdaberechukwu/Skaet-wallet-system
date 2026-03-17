package com.collins.Skaet_wallet_system.dtos.Request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequestDto {

    @NotEmpty(message = "Email is required for login")
    private String email;
    @NotEmpty(message = "Password ir required")
    private String password;
}
