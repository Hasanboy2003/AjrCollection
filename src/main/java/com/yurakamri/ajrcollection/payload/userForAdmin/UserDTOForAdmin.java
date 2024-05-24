package com.yurakamri.ajrcollection.payload.userForAdmin;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTOForAdmin {

    UUID id;

    String phoneNumber;

    String firstName;

    String lastName;

    UUID attachmentId;

    boolean active;

    LocalDateTime localDateTime;

    public UserDTOForAdmin(UUID id, String phoneNumber, String firstName, String lastName, boolean active) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = active;
    }
}
