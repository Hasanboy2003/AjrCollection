package com.yurakamri.ajrcollection.repository;

import com.yurakamri.ajrcollection.entity.User;
import com.yurakamri.ajrcollection.entity.enums.RoleEnum;
import com.yurakamri.ajrcollection.payload.interfaceDto.InterfaceUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
     @Query(value = "select count(u.id) from users u\n" +
             "    join role r on r.id = u.role_id\n" +
             "where r.role_name = 'ROLE_USER' ",nativeQuery = true)
     long countUsers();

     @Query(value = "select count(u.id) from users u\n" +
             "                            join role r on r.id = u.role_id\n" +
             "where r.role_name = 'ROLE_DELIVERER'",nativeQuery = true)
     long countDrivers();

    boolean existsByPhoneNumber(String phoneNumber);


    boolean existsByPhoneNumberAndEnabled(String phoneNumber, boolean enabled);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumberAndCodeAndEnabled(String phoneNumber, String code, boolean enabled);

    Optional<User> findByPhoneNumberAndEnabled(String phoneNumber, boolean enabled);

    Optional<User> findByIdAndEnabledFalse(UUID id);

    Optional<User> findByIdAndPhoneNumberAndCodeAndEnabled(UUID id, String phoneNumber, String code, boolean enabled);

    @Query(value = "select cast(u.id as varchar ) as id, u.phone_number phoneNumber, u.first_name firstName,u.last_name lastName,u.language from users u join orders o on u.id=o.user_id where o.id=:orderId", nativeQuery = true)
    InterfaceUserDto findByCartItemId(UUID orderId);

    List<User> findAllByRoleRoleName(RoleEnum role_roleName);
    Optional<User> findByIdAndRoleRoleName(UUID id, RoleEnum role_roleName);
    Page<User> findAllByRoleRoleName(Pageable pageable, RoleEnum role_roleName);


}
