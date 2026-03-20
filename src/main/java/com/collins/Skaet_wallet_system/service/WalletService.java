package com.collins.Skaet_wallet_system.service;

import com.collins.Skaet_wallet_system.dtos.Request.*;
import com.collins.Skaet_wallet_system.dtos.Response.LoginResponse;
import com.collins.Skaet_wallet_system.dtos.Response.TransactionResponse;
import com.collins.Skaet_wallet_system.dtos.Response.UserCreationResponse;
import com.collins.Skaet_wallet_system.dtos.Response.WalletResponse;
import com.collins.Skaet_wallet_system.dtos.ResponseDto;
import com.collins.Skaet_wallet_system.enums.TransactionDirection;
import com.collins.Skaet_wallet_system.enums.TransactionStatus;
import com.collins.Skaet_wallet_system.enums.TransactionType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface WalletService {

    ResponseEntity<ResponseDto<UserCreationResponse>> CreateUser(UserCreationRequest creationRequest);

    ResponseEntity<LoginResponse> loginUser(LoginRequest loginRequest);

    @Transactional
    ResponseEntity<WalletResponse> fundWallet(FundWalletRequest fundWalletRequest, String idempotencyKey);

    @Transactional
    ResponseEntity<WalletResponse> withdraw(WithdrawRequest request, String idempotencyKey);

    @Transactional
    ResponseEntity<String> transferFunds(TransferRequest request, String idempotencyKey);

    @Transactional
    ResponseEntity<TransactionResponse> reverseTransaction(ReverseTransactionRequest request, String idempotencyKey);

    ResponseEntity<Page<TransactionResponse>> getTransactionHistory(Long walletId,
                                                                    TransactionType type,
                                                                    TransactionStatus status,
                                                                    TransactionDirection direction,
                                                                    Pageable pageable);


}
