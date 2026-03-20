package com.collins.Skaet_wallet_system.repositories;

import com.collins.Skaet_wallet_system.enums.RoleType;
import com.collins.Skaet_wallet_system.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByRoleName(RoleType roleType);
}
