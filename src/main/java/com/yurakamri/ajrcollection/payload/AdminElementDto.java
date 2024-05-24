package com.yurakamri.ajrcollection.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminElementDto {
    long orders;
    long users;
    long drivers;
    long products;
    long incomes;
}
