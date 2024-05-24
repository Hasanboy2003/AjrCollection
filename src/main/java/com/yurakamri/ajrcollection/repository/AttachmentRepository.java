package com.yurakamri.ajrcollection.repository;

import com.yurakamri.ajrcollection.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {
}
