package com.collins.Skaet_wallet_system.repositories;

import com.collins.Skaet_wallet_system.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser,Long> {
    ApplicationUser findByEmail(String username);
}
