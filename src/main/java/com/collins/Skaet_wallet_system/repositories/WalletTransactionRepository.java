package com.collins.Skaet_wallet_system.repositories;

import com.collins.Skaet_wallet_system.enums.TransactionDirection;
import com.collins.Skaet_wallet_system.enums.TransactionStatus;
import com.collins.Skaet_wallet_system.enums.TransactionType;
import com.collins.Skaet_wallet_system.model.Wallet;
import com.collins.Skaet_wallet_system.model.WalletTransaction;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction,Long> {

    
    WalletTransaction findByReference(@NotBlank(message = "Transaction reference is required") String reference);

    Page<WalletTransaction> findAllByWallet(Wallet wallet, TransactionType type, TransactionStatus status, TransactionDirection direction, Pageable pageable);
}
