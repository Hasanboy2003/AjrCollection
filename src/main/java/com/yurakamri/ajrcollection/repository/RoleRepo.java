package com.yurakamri.ajrcollection.repository;

import com.yurakamri.ajrcollection.entity.Role;
import com.yurakamri.ajrcollection.entity.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(RoleEnum roleName);

    Role getByRoleName(RoleEnum roleName);

    boolean existsByRoleName(RoleEnum roleName);
}
