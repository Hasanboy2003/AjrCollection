package com.yurakamri.ajrcollection.payload.req.auth;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginDto {

    @Pattern(regexp = "^(\\+\\d{1,13})$", message = "PHONE NUMBER FORMAT MUST BE : (+998) XX XXX-XX-XX")
    String phoneNumber;
}
