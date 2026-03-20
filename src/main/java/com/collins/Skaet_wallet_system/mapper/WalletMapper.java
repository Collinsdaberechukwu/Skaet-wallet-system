package com.collins.Skaet_wallet_system.mapper;

import com.collins.Skaet_wallet_system.dtos.Response.TransactionResponse;
import com.collins.Skaet_wallet_system.dtos.Response.WalletResponse;
import com.collins.Skaet_wallet_system.model.Wallet;
import com.collins.Skaet_wallet_system.model.WalletTransaction;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {

    public WalletResponse toResponse(Wallet wallet){
        if(wallet == null) return null;

        WalletResponse response = new WalletResponse();
        response.setWalletId(wallet.getId());
        response.setWalletNumber(wallet.getWalletNumber());
        response.setBalance(wallet.getBalance());
        response.setStatus(wallet.getStatus());
        response.setDescription(wallet.getDescription());

        return response;
    }

    public TransactionResponse toTransactionResponse(WalletTransaction txn){
        if(txn == null) return null;

        TransactionResponse response = new TransactionResponse();
        response.setReference(txn.getReference());
        response.setAmount(txn.getAmount());
        response.setType(txn.getType());
        response.setStatus(txn.getStatus());
        response.setDirection(txn.getDirection());
        response.setDescription(txn.getDescription());
        response.setCreatedAt(txn.getCreatedAt());
        response.setWalletId(txn.getWallet().getId());

        return response;
    }
}
