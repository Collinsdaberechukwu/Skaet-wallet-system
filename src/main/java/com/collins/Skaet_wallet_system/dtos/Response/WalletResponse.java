package com.collins.Skaet_wallet_system.dtos.Response;

import com.collins.Skaet_wallet_system.enums.WalletStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletResponse {

    private Long walletId;
    private String walletNumber;
    private BigDecimal balance;
    private WalletStatus status;
    private String description;

}
