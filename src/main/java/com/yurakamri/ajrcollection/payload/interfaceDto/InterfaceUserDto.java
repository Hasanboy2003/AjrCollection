package com.yurakamri.ajrcollection.payload.interfaceDto;

import com.yurakamri.ajrcollection.entity.Attachment;
import com.yurakamri.ajrcollection.entity.enums.Lang;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

public interface InterfaceUserDto {

    UUID getId();
    String getPhoneNumber();
    String getFirstName();
    String getLastName();

//    @Value("#{@attachmentRepo.findByUserId(target.id)}")
//    InterfaceAttachmentDto getAttachment();
//    Lang getLanguage();
}
