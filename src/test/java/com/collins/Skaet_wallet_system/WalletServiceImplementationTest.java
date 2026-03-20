package com.collins.Skaet_wallet_system;

import com.collins.Skaet_wallet_system.config.SecurityConfig.SecurityServices.JwtService;
import com.collins.Skaet_wallet_system.dtos.Request.*;
import com.collins.Skaet_wallet_system.dtos.Response.LoginResponse;
import com.collins.Skaet_wallet_system.enums.RoleType;
import com.collins.Skaet_wallet_system.enums.WalletStatus;
import com.collins.Skaet_wallet_system.exception.WalletBusyException;
import com.collins.Skaet_wallet_system.mapper.WalletMapper;
import com.collins.Skaet_wallet_system.model.*;
import com.collins.Skaet_wallet_system.repositories.*;
import com.collins.Skaet_wallet_system.service.impl.WalletServiceImplementation;
import com.collins.Skaet_wallet_system.util.ReferenceGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class WalletServiceImplementationTest {

    @InjectMocks
    private WalletServiceImplementation walletService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private WalletTransactionRepository walletTransactionRepository;
    @Mock
    private IdempotencyKeyRepository idempotencyKeyRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ReferenceGenerator referenceGenerator;
    @Mock
    private ApplicationEventPublisher publisher;
    @Mock
    private WalletMapper walletMapper;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @BeforeEach
    void setup() {
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }



    @Test
    void createUser_success() {

        UserCreationRequest request = new UserCreationRequest();
        request.setEmail("collinstest@gmail.com");
        request.setPassword("password");
        request.setUserName("collins");

        Role role = new Role();
        role.setRoleName(RoleType.USER);

        ApplicationUser user = new ApplicationUser();
        user.setId(1L);
        user.setUserName("collins");

        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setStatus(WalletStatus.ACTIVE);

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(roleRepository.findByRoleName(RoleType.USER)).thenReturn(role);
        when(userRepository.save(any())).thenReturn(user);
        when(walletRepository.save(any())).thenReturn(wallet);
        when(referenceGenerator.generateWalletNumber()).thenReturn("123456");

        ResponseEntity<?> response = walletService.CreateUser(request);

        assertEquals(201, response.getStatusCode().value());
        verify(userRepository).save(any());
        verify(walletRepository).save(any());
    }


    @Test
    void login_success() {

        LoginRequest request = new LoginRequest();
        request.setEmail("collinsdaberechi20@gmail.com");
        request.setPassword("123456789");

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtService.generateToken(any())).thenReturn("token");

        ResponseEntity<LoginResponse> response = walletService.loginUser(request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("token", response.getBody().getToken());
    }



    @Test
    void fundWallet_success() {

        FundWalletRequest request = new FundWalletRequest();
        request.setWalletId(1L);
        request.setAmount(BigDecimal.valueOf(100));

        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(BigDecimal.ZERO);

        when(redisTemplate.hasKey(any())).thenReturn(false);
        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        when(valueOperations.setIfAbsent(any(), any(), any(Duration.class))).thenReturn(true);
        when(referenceGenerator.generate()).thenReturn("ref");

        ResponseEntity<?> response = walletService.fundWallet(request, "idem1");

        assertEquals(200, response.getStatusCode().value());
        verify(walletRepository).save(any());
        verify(walletTransactionRepository).save(any());
    }



    @Test
    void withdraw_success() {

        WithdrawRequest request = new WithdrawRequest();
        request.setWalletId(1L);
        request.setAmount(BigDecimal.valueOf(50));

        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(BigDecimal.valueOf(100));

        when(redisTemplate.hasKey(any())).thenReturn(false);
        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        when(valueOperations.setIfAbsent(any(), any(), any(Duration.class))).thenReturn(true);
        when(referenceGenerator.generate()).thenReturn("ref");

        ResponseEntity<?> response = walletService.withdraw(request, "idem1");

        assertEquals(200, response.getStatusCode().value());
    }



    @Test
    void transferFunds_success() {

        TransferRequest request = new TransferRequest();
        request.setSourceWalletId(1L);
        request.setDestinationWalletId(2L);
        request.setAmount(BigDecimal.valueOf(50));

        Wallet source = new Wallet();
        source.setId(1L);
        source.setBalance(BigDecimal.valueOf(100));

        Wallet destination = new Wallet();
        destination.setId(2L);
        destination.setBalance(BigDecimal.ZERO);

        when(redisTemplate.hasKey(any())).thenReturn(false);
        when(walletRepository.findById(1L)).thenReturn(Optional.of(source));
        when(walletRepository.findById(2L)).thenReturn(Optional.of(destination));
        when(valueOperations.setIfAbsent(any(), any(), any(Duration.class))).thenReturn(true);
        when(referenceGenerator.generate()).thenReturn("ref");

        ResponseEntity<String> response =
                walletService.transferFunds(request, "idem1");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("ref", response.getBody());
    }



    @Test
    void transfer_sameWallet_shouldThrowException() {

        TransferRequest request = new TransferRequest();
        request.setSourceWalletId(1L);
        request.setDestinationWalletId(1L);
        request.setAmount(BigDecimal.valueOf(10));

        assertThrows(WalletBusyException.class,
                () -> walletService.transferFunds(request, "key"));
    }

}