package com.collins.Skaet_wallet_system.dtos.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReverseTransactionRequest {

    @NotBlank(message = "Transaction reference is required")
    private String reference;

    @Size(max = 255)
    private String reason;
}
