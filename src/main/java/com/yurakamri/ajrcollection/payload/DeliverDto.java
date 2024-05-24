package com.yurakamri.ajrcollection.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliverDto {
    UUID id;
    String phoneNumber;
    String firstName;
    String lastName;
    UUID attachmentId;
    LocalDateTime createdAt;
    boolean active;
}
