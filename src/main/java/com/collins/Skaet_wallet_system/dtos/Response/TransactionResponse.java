package com.collins.Skaet_wallet_system.dtos.Response;

import com.collins.Skaet_wallet_system.enums.TransactionDirection;
import com.collins.Skaet_wallet_system.enums.TransactionStatus;
import com.collins.Skaet_wallet_system.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponse {

    private Long walletId;
    private String reference;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionStatus status;
    private TransactionDirection direction;
    private String description;
    private LocalDateTime createdAt;
}
