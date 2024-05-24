package com.yurakamri.ajrcollection.repository;

import com.yurakamri.ajrcollection.entity.SmsForToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SmsForApiRepo extends JpaRepository<SmsForToken, Long> {
    @Query(value = "SELECT max(id) FROM SmsForToken")
    Long maxId();
}
