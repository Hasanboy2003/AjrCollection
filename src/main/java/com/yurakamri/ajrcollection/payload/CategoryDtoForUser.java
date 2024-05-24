package com.yurakamri.ajrcollection.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDtoForUser {

    UUID uuid;

    String name;

}
