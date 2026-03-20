package com.collins.Skaet_wallet_system.repositories;

import com.collins.Skaet_wallet_system.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {
}
