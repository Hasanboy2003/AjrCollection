package com.yurakamri.ajrcollection.payload.home;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandDtoForHome {
    private Long id;
    private String name;
}
