package com.yurakamri.ajrcollection.repository;

import com.yurakamri.ajrcollection.entity.Advertising;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdvertisingRepository extends JpaRepository<Advertising, UUID> {
}
