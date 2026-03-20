package com.collins.Skaet_wallet_system.model;

import com.collins.Skaet_wallet_system.enums.WalletStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Wallet extends BaseEntity{

    @Column(unique = true, nullable = false)
    private String walletNumber;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WalletStatus status;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WalletTransaction> transactions = new ArrayList<>();
}
