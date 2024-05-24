package com.yurakamri.ajrcollection.repository;

import com.yurakamri.ajrcollection.entity.Attachment;
import com.yurakamri.ajrcollection.payload.interfaceDto.InterfaceAttachmentDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AttachmentRepo extends JpaRepository<Attachment, UUID> {

    @Query(
            nativeQuery = true,
            value = "select cast(a.id as varchar) as attachmentId\n" +
                    "                    from attachment a\n" +
                    "                             join product_attachment pa on a.id = pa.attachment_id\n" +
                    "                             join product_color pc on pa.product_color_id = pc.id\n" +
                    "                    where pc.id = :productColorId"
    )
    List<UUID> getAttachmentIdListByProductColorId(@Param("productColorId") UUID productColorId);

    @Query(value = "select cast (a.id as varchar ),a.original_name originalName,a.generated_name generatedName,a.content_type contentType,a.size,a.file_location fileLocation from attachment a join users u on u.attachment_id =a.id where u.id=:userId", nativeQuery = true)
    InterfaceAttachmentDto findByUserId(UUID userId);
    
}
