package com.collins.Skaet_wallet_system.config.SecurityConfig.SecurityServices;

import com.collins.Skaet_wallet_system.model.ApplicationUser;
import com.collins.Skaet_wallet_system.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        ApplicationUser applicationUser = userRepository.findByEmail(username);
        if (applicationUser == null){
            throw new UsernameNotFoundException("User not found with email :" + username);
        }
        return new CustomUserDetails(applicationUser);
    }
}
