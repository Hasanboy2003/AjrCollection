package com.yurakamri.ajrcollection.payload.interfaceDto;

import javax.persistence.Column;
import java.util.UUID;

public interface InterfaceAttachmentDto {
    UUID getId();
    String getOriginalName();
    String getGeneratedName();
    String getContentType();
    long getSize();
    String getFileLocation();
}
