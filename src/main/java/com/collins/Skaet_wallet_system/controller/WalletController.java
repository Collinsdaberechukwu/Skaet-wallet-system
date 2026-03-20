package com.collins.Skaet_wallet_system.controller;

import com.collins.Skaet_wallet_system.dtos.Request.*;
import com.collins.Skaet_wallet_system.dtos.Response.LoginResponse;
import com.collins.Skaet_wallet_system.dtos.Response.TransactionResponse;
import com.collins.Skaet_wallet_system.dtos.Response.UserCreationResponse;
import com.collins.Skaet_wallet_system.dtos.Response.WalletResponse;
import com.collins.Skaet_wallet_system.dtos.ResponseDto;
import com.collins.Skaet_wallet_system.enums.TransactionDirection;
import com.collins.Skaet_wallet_system.enums.TransactionStatus;
import com.collins.Skaet_wallet_system.enums.TransactionType;
import com.collins.Skaet_wallet_system.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wallets")
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/user_creation")
    public ResponseEntity<ResponseDto<UserCreationResponse>> CreateUser(@RequestBody @Valid UserCreationRequest creationRequest){
       return walletService.CreateUser(creationRequest);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){
        return walletService.loginUser(request);
    }


    @PostMapping("/fund")
    public ResponseEntity<WalletResponse> fundWallet(
                                                        @Valid @RequestBody FundWalletRequest request,
                                                        @RequestHeader("Idempotency-Key") String idempotencyKey){
        return walletService.fundWallet(request, idempotencyKey);
    }


    @PostMapping("/withdraw")
    public ResponseEntity<WalletResponse> withdraw(
                                                    @Valid @RequestBody WithdrawRequest request,
                                                    @RequestHeader("Idempotency-Key") String idempotencyKey){
        return walletService.withdraw(request, idempotencyKey);
    }


    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(
                                            @Valid @RequestBody TransferRequest request,
                                            @RequestHeader("Idempotency-Key") String idempotencyKey){
        return walletService.transferFunds(request, idempotencyKey);
    }

    @PostMapping("/reverse")
    public ResponseEntity<TransactionResponse> reverseTransaction(
                                                    @Valid @RequestBody ReverseTransactionRequest request,
                                                    @RequestHeader("Idempotency-Key") String idempotencyKey){
        return walletService.reverseTransaction(request, idempotencyKey);
    }


    @GetMapping("/{walletId}/transactions")
    public ResponseEntity<Page<TransactionResponse>> getTransactionHistory(
                                                                @PathVariable Long walletId,
                                                                @RequestParam(required = false) TransactionType type,
                                                                @RequestParam(required = false) TransactionStatus status,
                                                                @RequestParam(required = false) TransactionDirection direction,
                                                                Pageable pageable){
        return walletService.getTransactionHistory(walletId, type, status, direction, pageable);
    }

}
