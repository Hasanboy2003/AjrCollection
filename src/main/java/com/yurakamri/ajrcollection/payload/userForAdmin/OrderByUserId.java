package com.yurakamri.ajrcollection.payload.userForAdmin;

import com.yurakamri.ajrcollection.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderByUserId {

    private UUID orderId;

    private String code;

    private String deliverer;

    private String lat;

    private String lan;

    private Long orderStatusId;

    private LocalDateTime updateTime;
}
