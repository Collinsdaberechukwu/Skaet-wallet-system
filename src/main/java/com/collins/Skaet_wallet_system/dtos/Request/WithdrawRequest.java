package com.collins.Skaet_wallet_system.dtos.Request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawRequest {
    @NotNull(message = "Wallet ID is required")
    private Long walletId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01",message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "description required")
    private String description;
}
