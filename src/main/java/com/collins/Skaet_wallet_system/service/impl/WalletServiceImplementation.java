package com.collins.Skaet_wallet_system.service.impl;

import com.collins.Skaet_wallet_system.config.SecurityConfig.SecurityServices.JwtService;
import com.collins.Skaet_wallet_system.dtos.Request.*;
import com.collins.Skaet_wallet_system.dtos.Response.LoginResponse;
import com.collins.Skaet_wallet_system.dtos.Response.TransactionResponse;
import com.collins.Skaet_wallet_system.dtos.Response.UserCreationResponse;
import com.collins.Skaet_wallet_system.dtos.Response.WalletResponse;
import com.collins.Skaet_wallet_system.dtos.ResponseDto;
import com.collins.Skaet_wallet_system.enums.*;
import com.collins.Skaet_wallet_system.event.UserCreatedEvent;
import com.collins.Skaet_wallet_system.event.UserWalletCreatedEvent;
import com.collins.Skaet_wallet_system.exception.*;
import com.collins.Skaet_wallet_system.mapper.UserMapper;
import com.collins.Skaet_wallet_system.mapper.WalletMapper;
import com.collins.Skaet_wallet_system.model.*;
import com.collins.Skaet_wallet_system.repositories.*;
import com.collins.Skaet_wallet_system.service.WalletService;
import com.collins.Skaet_wallet_system.util.ReferenceGenerator;
//import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImplementation implements WalletService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final RoleRepository roleRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final IdempotencyKeyRepository idempotencyKeyRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RedisTemplate<String,Object> redisTemplate;
    private final ReferenceGenerator referenceGenerator;
    private final ApplicationEventPublisher publisher;
    private final WalletMapper walletMapper;


    @Override
    public ResponseEntity<ResponseDto<UserCreationResponse>> CreateUser(UserCreationRequest creationRequest){
        log.info("Creating user wih email: {}", creationRequest.getEmail());

        if (userRepository.existsByEmail(creationRequest.getEmail())){
            throw new UserAlreadyExistException("User with Email already exist" + creationRequest.getEmail());
        }

        ApplicationUser applicationUser = UserMapper.mapToUser(creationRequest);
        applicationUser.setPassword(passwordEncoder.encode(creationRequest.getPassword()));

        Role role = roleRepository.findByRoleName(RoleType.USER);
        if (role == null){
            throw new RoleResourceNotFoundException("Role not found");
        }
        applicationUser.setRole(role);

        ApplicationUser savedApplicationUser = userRepository.save(applicationUser);
        publisher.publishEvent(new UserCreatedEvent(savedApplicationUser));
        log.info("User {} successfully", creationRequest.getEmail());


        Wallet newWallet = new Wallet();
        newWallet.setWalletNumber(referenceGenerator.generateWalletNumber());
        newWallet.setBalance(BigDecimal.ZERO);
        newWallet.setStatus(WalletStatus.ACTIVE);
        newWallet.setDescription("primary wallet for the user : " + applicationUser.getUserName());
        newWallet.setUser(applicationUser);

        newWallet = walletRepository.save(newWallet);
        publisher.publishEvent(new UserWalletCreatedEvent(newWallet));
        log.info("User wallet {} successfully", creationRequest.getEmail());

        UserCreationResponse userCreationResponse = UserMapper.mapToUserResponse(savedApplicationUser);
        ResponseDto<UserCreationResponse> responseDto = ResponseDto.<UserCreationResponse>builder()
                .data(userCreationResponse)
                .statusCode(String.valueOf(HttpStatus.CREATED))
                .statusMessage("User with generated wallet number created successfully" + applicationUser.getUserName())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }

//    @RateLimiter(name = "authLimiter", fallbackMethod = "loginFallback")
    @Override
    public ResponseEntity<LoginResponse> loginUser(LoginRequest loginRequest){
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        if (authenticate.isAuthenticated()){
            String jwtToken = jwtService.generateToken(loginRequest);

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setWelcomeMessage("Login Successful welcome to Skaet wallet :" + loginRequest.getEmail());
            loginResponse.setToken(jwtToken);
            return ResponseEntity.ok(loginResponse);
        }else {
            throw new ResourceNotFoundException("Invalid login credentials for email :" + loginRequest.getEmail());
        }
    }

//    @RateLimiter(name = "walletLimiter", fallbackMethod = "walletFallback")
    @Transactional
    @Override
    public ResponseEntity<WalletResponse> fundWallet(FundWalletRequest fundWalletRequest, String idempotencyKey){
        String redisKey = "idem:" + idempotencyKey;
          log.info("Funding wallet {} with amount {}", fundWalletRequest.getWalletId(), fundWalletRequest.getAmount());

        if(Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))){
        log.info("Duplicate fund request detected for wallet {}", fundWalletRequest.getWalletId());
        throw new DuplicateFundException("Duplicate request");
    }

    Wallet wallet = walletRepository.findById(fundWalletRequest.getWalletId())
            .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));


    String lockKey = "lock:wallet:" + wallet.getId();
    Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", Duration.ofSeconds(10));
        if(!Boolean.TRUE.equals(locked)){
        throw new WalletBusyException("Wallet is busy, retry later");
    }

        try {
        if(fundWalletRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new InsufficientAmountException("Amount must be > 0");
        }

        wallet.setBalance(wallet.getBalance().add(fundWalletRequest.getAmount()));

        WalletTransaction txn = new WalletTransaction();
        txn.setWallet(wallet);
        txn.setAmount(fundWalletRequest.getAmount());
        txn.setType(TransactionType.FUNDING);
        txn.setDirection(TransactionDirection.CREDIT);
        txn.setStatus(TransactionStatus.SUCCESS);
        txn.setReference(referenceGenerator.generate());
        txn.setDescription(fundWalletRequest.getDescription());

        walletTransactionRepository.save(txn);
        walletRepository.save(wallet);

        IdempotencyKey key = new IdempotencyKey();
        key.setIdempotencyKey(idempotencyKey);
        key.setRequestHash(fundWalletRequest.toString());
        key.setResponseBody("SUCCESS");
        key.setStatus(IdempotencyStatus.SUCCESS);
        idempotencyKeyRepository.save(key);

        redisTemplate.opsForValue().set(redisKey, "done", Duration.ofMinutes(10));

        log.info("Fund successful for wallet {}. New balance: {}", wallet.getWalletNumber(), wallet.getBalance());

        return ResponseEntity.ok(walletMapper.toResponse(wallet));

    } finally {
        redisTemplate.delete(lockKey);
    }
}

//    @RateLimiter(name = "withdrawLimiter", fallbackMethod = "withdrawFallback")
    @Transactional
    @Override
    public ResponseEntity<WalletResponse> withdraw(WithdrawRequest request, String idempotencyKey) {

        log.info("Withdraw request wallet {} amount {}", request.getWalletId(), request.getAmount());

        String redisKey = "idem:" + idempotencyKey;

        if(Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))){
            throw new DuplicateFundException("Duplicate request");
        }

        Wallet wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        String lockKey = "lock:wallet:" + wallet.getId();

        Boolean locked = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, "locked", Duration.ofSeconds(10));

        if(!Boolean.TRUE.equals(locked)){
            throw new WalletBusyException("Wallet busy, retry");
        }

        try {

            if(wallet.getBalance().compareTo(request.getAmount()) < 0){
                throw new InsufficientAmountException("Insufficient balance");
            }

            wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));

            WalletTransaction txn = buildTxn(
                    wallet,
                    request.getAmount(),
                    referenceGenerator.generate(),
                    TransactionType.WITHDRAWAL,
                    TransactionDirection.DEBIT,
                    request.getDescription()
            );

            walletTransactionRepository.save(txn);
            walletRepository.save(wallet);

            saveIdempotency(idempotencyKey, request.toString(), "SUCCESS");

            redisTemplate.opsForValue().set(redisKey, "done", Duration.ofMinutes(10));

            log.info("Withdraw successful. Wallet: {} Balance: {}", wallet.getWalletNumber(), wallet.getBalance());

            return ResponseEntity.ok(walletMapper.toResponse(wallet));

        } catch (Exception ex){
            log.error("Withdraw failed: {}", ex.getMessage(), ex);
            throw ex;
        } finally {
            redisTemplate.delete(lockKey);
        }
    }

    private void saveIdempotency(String key, String request, String response){

        log.info("Saving idempotency key: {}", key);

        IdempotencyKey entity = new IdempotencyKey();
        entity.setIdempotencyKey(key);
        entity.setRequestHash(request);
        entity.setResponseBody(response);
        entity.setStatus(IdempotencyStatus.SUCCESS);

        idempotencyKeyRepository.save(entity);
    }

//    @RateLimiter(name = "transferLimiter", fallbackMethod = "transferFallback")
    @Transactional
    @Override
    public ResponseEntity<String> transferFunds(TransferRequest request, String idempotencyKey) {

        log.info("Transfer request: {} -> {} amount {}",
                request.getSourceWalletId(),
                request.getDestinationWalletId(),
                request.getAmount());

        String redisKey = "idem:" + idempotencyKey;

        if(Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))){
            log.info("Duplicate transfer request detected");
            throw new DuplicateFundException("Duplicate request");
        }

        if(request.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new InsufficientAmountException("Amount must be greater than zero");
        }

        if(request.getSourceWalletId().equals(request.getDestinationWalletId())){
            throw new WalletBusyException("Cannot transfer to same wallet");
        }

        Wallet source = walletRepository.findById(request.getSourceWalletId())
                .orElseThrow(() -> new ResourceNotFoundException("Source wallet not found"));

        Wallet destination = walletRepository.findById(request.getDestinationWalletId())
                .orElseThrow(() -> new ResourceNotFoundException("Destination wallet not found"));


        Wallet firstLock = source.getId() < destination.getId() ? source : destination;
        Wallet secondLock = source.getId() < destination.getId() ? destination : source;

        String firstLockKey = "lock:wallet:" + firstLock.getId();
        String secondLockKey = "lock:wallet:" + secondLock.getId();

        Boolean firstLocked = redisTemplate.opsForValue()
                .setIfAbsent(firstLockKey, "locked", Duration.ofSeconds(10));

        if(!Boolean.TRUE.equals(firstLocked)){
            throw new WalletBusyException("Wallet busy, retry");
        }

        Boolean secondLocked = redisTemplate.opsForValue()
                .setIfAbsent(secondLockKey, "locked", Duration.ofSeconds(10));

        if(!Boolean.TRUE.equals(secondLocked)){
            redisTemplate.delete(firstLockKey); // release first lock
            throw new WalletBusyException("Wallet busy, retry");
        }

        try {
            if(source.getBalance().compareTo(request.getAmount()) < 0){
                throw new InsufficientAmountException("Insufficient balance");
            }

            String reference = referenceGenerator.generate();


            source.setBalance(source.getBalance().subtract(request.getAmount()));
            destination.setBalance(destination.getBalance().add(request.getAmount()));


            WalletTransaction debitTxn = buildTxn(
                    source,
                    request.getAmount(),
                    reference,
                    TransactionType.TRANSFER,
                    TransactionDirection.DEBIT,
                    request.getDescription()
            );

            WalletTransaction creditTxn = buildTxn(
                    destination,
                    request.getAmount(),
                    reference,
                    TransactionType.TRANSFER,
                    TransactionDirection.CREDIT,
                    request.getDescription()
            );

            walletTransactionRepository.saveAll(List.of(debitTxn, creditTxn));
            walletRepository.saveAll(List.of(source, destination));

            IdempotencyKey key = new IdempotencyKey();
            key.setIdempotencyKey(idempotencyKey);
            key.setRequestHash(request.toString());
            key.setResponseBody(reference);
            key.setStatus(IdempotencyStatus.SUCCESS);
            idempotencyKeyRepository.save(key);

            redisTemplate.opsForValue().set(redisKey, "done", Duration.ofMinutes(10));

            log.info("Transfer successful. Ref: {} | Source balance: {} | Destination balance: {}",
                    reference, source.getBalance(), destination.getBalance());

            return ResponseEntity.ok(reference);

        } catch (Exception ex){
            log.error("Transfer failed: {}", ex.getMessage(), ex);
            throw ex;
        } finally {
            redisTemplate.delete(firstLockKey);
            redisTemplate.delete(secondLockKey);
        }
    }

    private WalletTransaction buildTxn(
            Wallet wallet,
            BigDecimal amount,
            String reference,
            TransactionType type,
            TransactionDirection direction,
            String description
    ) {
        WalletTransaction txn = new WalletTransaction();

        txn.setWallet(wallet);
        txn.setAmount(amount);
        txn.setReference(reference);
        txn.setType(type);
        txn.setDirection(direction);
        txn.setStatus(TransactionStatus.SUCCESS);
        txn.setDescription(description);

        return txn;
    }


//    @RateLimiter(name = "reverseLimiter", fallbackMethod = "reverseFallback")
    @Transactional
    @Override
    public ResponseEntity<TransactionResponse> reverseTransaction(ReverseTransactionRequest request, String idempotencyKey) {

    log.info("Reversing transaction {}", request.getReference());

    WalletTransaction walletTransaction = walletTransactionRepository.findByReference(request.getReference());
    if (walletTransaction == null){
        throw new ResourceNotFoundException("TRANSACTION NOT FOUND");
    }


    if(walletTransaction.getStatus() == TransactionStatus.REVERSED){
        throw new AlreadyReservedException("Transaction already reversed");
    }

    Wallet wallet = walletTransaction.getWallet();


    String lockKey = "lock:wallet:" + wallet.getId();
    Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", Duration.ofSeconds(10));
    if(!Boolean.TRUE.equals(locked)){
        throw new WalletBusyException("Wallet busy, retry");
    }

    try {
        BigDecimal reversedAmount = walletTransaction.getAmount();

        if(walletTransaction.getDirection() == TransactionDirection.CREDIT){
            wallet.setBalance(wallet.getBalance().subtract(reversedAmount));
        } else {
            wallet.setBalance(wallet.getBalance().add(reversedAmount));
        }


        WalletTransaction reverseTxn = new WalletTransaction();
        reverseTxn.setWallet(wallet);
        reverseTxn.setAmount(reversedAmount);
        reverseTxn.setReference(referenceGenerator.generate());
        reverseTxn.setType(walletTransaction.getType());
        reverseTxn.setDirection(walletTransaction.getDirection() == TransactionDirection.CREDIT ? TransactionDirection.DEBIT : TransactionDirection.CREDIT);
        reverseTxn.setStatus(TransactionStatus.SUCCESS);
        reverseTxn.setOriginalTransactionReference(walletTransaction.getReference());
        reverseTxn.setDescription(request.getReason());

        walletTransactionRepository.save(reverseTxn);
        walletRepository.save(wallet);

        walletTransaction.setStatus(TransactionStatus.REVERSED);
        walletTransactionRepository.save(walletTransaction);

        log.info("Transaction {} reversed successfully", request.getReference());

        return ResponseEntity.ok(walletMapper.toTransactionResponse(reverseTxn));

    } finally {
        redisTemplate.delete(lockKey);
    }
}

@Override
public ResponseEntity<Page<TransactionResponse>> getTransactionHistory(Long walletId,
                                                                       TransactionType type,
                                                                       TransactionStatus status,
                                                                       TransactionDirection direction,
                                                                       Pageable pageable) {

    log.info("Fetching transaction history for wallet {}", walletId);

    Wallet wallet = walletRepository.findById(walletId)
            .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

    Page<WalletTransaction> transactions = walletTransactionRepository.findAllByWallet(wallet, type, status, direction, pageable);

    Page<TransactionResponse> response = transactions.map(walletMapper::toTransactionResponse);

    return ResponseEntity.ok(response);
}
}

