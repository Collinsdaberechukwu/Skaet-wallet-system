package com.collins.Skaet_wallet_system.model;

import com.collins.Skaet_wallet_system.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "roles",uniqueConstraints = @UniqueConstraint(columnNames = "role_name"))
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity{

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name",nullable = false)
    private RoleType roleName;

    @OneToMany(mappedBy = "role")
    private List<ApplicationUser> applicationUser;
}
