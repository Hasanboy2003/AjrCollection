package com.yurakamri.ajrcollection.repository;

import com.yurakamri.ajrcollection.entity.OrderStatus;
import com.yurakamri.ajrcollection.entity.enums.OrderStatusEnum;
import com.yurakamri.ajrcollection.payload.interfaceDto.InterfaceStatusDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface OrderStatusRepo extends JpaRepository<OrderStatus, Long> {
    OrderStatus getByName(OrderStatusEnum name);

    //DO NOT TOUCH
    @Query(value = "select  os.name from order_status os join orders o on o.order_status_id = os.id where o.id =:orderId", nativeQuery = true)
    String findByOrderId(UUID orderId);

    boolean existsByName(OrderStatusEnum name);
}
