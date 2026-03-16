package com.collins.Skaet_wallet_system.model;

import com.collins.Skaet_wallet_system.enums.TransactionDirection;
import com.collins.Skaet_wallet_system.enums.TransactionStatus;
import com.collins.Skaet_wallet_system.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WalletTransaction extends BaseEntity{

    @Column(unique = true, nullable = false)
    private String reference;

    private BigDecimal amount;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private String originalTransactionReference;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    private TransactionDirection direction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;
}
