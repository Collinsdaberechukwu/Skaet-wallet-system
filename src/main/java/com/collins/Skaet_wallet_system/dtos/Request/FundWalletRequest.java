package com.collins.Skaet_wallet_system.dtos.Request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FundWalletRequest {

    @NotNull(message = "Wallet ID required")
    private Long walletId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01",message = "Amount must be grater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Transfer description is required")
    @Size(max = 255)
    private String description;
}
