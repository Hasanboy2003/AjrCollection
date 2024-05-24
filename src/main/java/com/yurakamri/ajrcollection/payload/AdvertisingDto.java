package com.yurakamri.ajrcollection.payload;

import com.yurakamri.ajrcollection.entity.Attachment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdvertisingDto {

    UUID id;

    @NotNull
    UUID attachmentId;

    @NotNull
    String title;

    @Column(columnDefinition = "TEXT")
    String description;

    String destination;

    //yangi field qo`shdik. Userga faqat aktiv reklamalar ko`rinadigan qilish uchun
    boolean active;
}
