package com.yurakamri.ajrcollection.payload;

import com.yurakamri.ajrcollection.entity.enums.Lang;
import com.yurakamri.ajrcollection.payload.product.ProductDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    UUID id;
    String phoneNumber;
    String firstName;
    String lastName;
    UUID attachmentId;
    Lang language = Lang.UZ;
    List<ProductDto> favourites;


    public UserDto(UUID id, String phoneNumber, String firstName, String lastName, UUID attachmentId, Lang language) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.attachmentId = attachmentId;
        this.language = language;
    }

}
