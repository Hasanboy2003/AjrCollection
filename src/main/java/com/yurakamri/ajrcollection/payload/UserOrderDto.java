package com.yurakamri.ajrcollection.payload;

import com.yurakamri.ajrcollection.payload.interfaceDto.OrderProjection;
import com.yurakamri.ajrcollection.payload.userForAdmin.UserDTOForAdmin;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserOrderDto {
    UserDTOForAdmin user;
    List<OrderProjection> orderList;
}
