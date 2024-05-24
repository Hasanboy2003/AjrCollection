package com.yurakamri.ajrcollection.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeProfile {

    @Pattern(regexp = "^(\\+\\d{1,13})$", message = "PHONE NUMBER FORMAT MUST BE : (+998) XX XXX-XX-XX")
    private String phoneNumber;

    private String firstName;

    private String lastName;

}
