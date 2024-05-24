package com.yurakamri.ajrcollection.repository;

import com.yurakamri.ajrcollection.entity.ChangePhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ChangePhoneNumberRepo extends JpaRepository<ChangePhoneNumber, UUID> {

    Optional<ChangePhoneNumber> findByTempPhoneNumberAndCodeAndVerifiedFalseAndActiveTrueAndUser_Id(String tempPhoneNumber, String code, UUID user_id);

    Optional<ChangePhoneNumber> findByTempPhoneNumberAndCodeIsNotNullAndVerifiedFalseAndActiveFalseAndUser_Id(String phoneNumber, UUID id);
}
