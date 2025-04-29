package com.project.ecommercebackend.repository;

import com.project.ecommercebackend.model.enums.AppRole;
import com.project.ecommercebackend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
