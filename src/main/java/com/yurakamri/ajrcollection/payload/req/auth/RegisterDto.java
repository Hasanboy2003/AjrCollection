package com.yurakamri.ajrcollection.payload.req.auth;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterDto {

    @Size(min = 2, message = "FIRST NAME LENGTH MUST BE AT LEAST 2")
    @NotBlank(message = "FIRST NAME MUST NOT BE BLANK")
    String firstName;
    String lastName;
    @Pattern(regexp = "^(\\+\\d{1,13})$", message = "PHONE NUMBER FORMAT MUST BE : (+998) XX XXX-XX-XX")
    @Size(min = 13, max = 13, message = "LENGTH MUST BE 13")
    String phoneNumber;
}
