package com.yurakamri.ajrcollection.repository;

import com.yurakamri.ajrcollection.entity.Order;
import com.yurakamri.ajrcollection.payload.interfaceDto.InterfaceDriverOrderDto;
import com.yurakamri.ajrcollection.payload.interfaceDto.InterfaceOrderDto;
import com.yurakamri.ajrcollection.payload.interfaceDto.OrderProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface OrderRepo extends JpaRepository<Order, UUID> {
    @Query(value = "select count(id) from orders",nativeQuery = true)
    long countOrders();
    //DO NOT TOUCH
    @Query(value = "select cast(o.id as varchar) as          id,\n" +
            "       o.code,\n" +
            "       o.address,\n" +
            "       cast (o.created_at as date)                     createdAt,\n" +
            "       o.latitude,\n" +
            "       o.longitude,\n" +
            "       o.phone_number        as          phoneNumber,\n" +
            "       o.shipping_amount     as          shippingAmount,\n" +
            "(select sum(ci1.fixed_price * ci1.quantity) as charityCart from cart_item ci1\n" +
            "join orders o2 on ci1.order_id = o2.id\n" +
            "where o2.id = o.id and ci1.for_charity = true),\n" +
            "       \n" +
            "(select sum(ci2.fixed_price * ci2.quantity) as myCart from cart_item ci2\n" +
            "join orders o2 on ci2.order_id = o2.id\n" +
            "where o2.id = o.id  and ci2.for_charity = false),\n" +
            "       \n" +
            "sum(ci.quantity * ci.fixed_price) totalMoney from orders o\n" +
            "join cart_item ci on o.id = ci.order_id\n" +
            "\n" +
            "group by o.id,o.code  order by o.code limit :size offset :page", nativeQuery = true)
    List<InterfaceOrderDto> getOrders(int size, int page);

    //DO NOT TOUCH
    @Query(value = "select cast(o.id as varchar) as          id,\n" +
            "                                           o.code,\n" +
            "                                           o.address,\n" +
            "                                           cast (o.created_at as date)                     createdAt,\n" +
            "                                           o.latitude,\n" +
            "                                           o.longitude,\n" +
            "                                           o.phone_number        as          phoneNumber,\n" +
            "                                           o.shipping_amount     as          shippingAmount,\n" +
            "                                    (select cast(d.phone_number as varchar) as driverPhoneNumber from users d\n" +
            "                                    join orders o3 on d.id = o3.driver_id\n" +
            "                                    where o3.id = o.id),\n" +
            "                                    (select sum(ci1.fixed_price * ci1.quantity) as charityCart from cart_item ci1\n" +
            "                                    join orders o2 on ci1.order_id = o2.id\n" +
            "                                    join users u2 on u2.id = ci1.user_id\n" +
            "                                    where o2.id = o.id and ci1.user_id = u2.id and u2.id = :userId and ci1.for_charity = true),\n" +
            "\n" +
            "                                    (select sum(ci2.fixed_price * ci2.quantity) as myCart from cart_item ci2\n" +
            "                                    join orders o2 on ci2.order_id = o2.id\n" +
            "                                    join users u2 on u2.id = ci2.user_id where o2.id = o.id and ci2.user_id = u2.id and u2.id = :userId and ci2.for_charity = false),\n" +
            "\n" +
            "                                    sum(ci.quantity * ci.fixed_price) totalMoney from orders o\n" +
            "                                    join cart_item ci on o.id = ci.order_id\n" +
            "                                    join users u on ci.user_id = u.id\n" +
            "                                    where u.id =:userId  and o.user_id = u.id\n" +
            "                                    group by o.id;", nativeQuery = true)
    List<InterfaceOrderDto> findByUserId(UUID userId);

    @Query(value = "select cast(o.id as varchar) as          id,\n" +
            "                                           o.code,\n" +
            "                                           o.address,\n" +
            "                                           cast (o.created_at as date)                     createdAt,\n" +
            "                                           o.latitude,\n" +
            "                                           o.longitude,\n" +
            "                                           o.phone_number        as          phoneNumber,\n" +
            "                                           o.shipping_amount     as          shippingAmount,\n" +
            "                                    (select cast(d.phone_number as varchar) as driverPhoneNumber from users d\n" +
            "                                    join orders o3 on d.id = o3.driver_id\n" +
            "                                    where o3.id = o.id),\n" +
            "                                    (select sum(ci1.fixed_price * ci1.quantity) as charityCart from cart_item ci1\n" +
            "                                    join orders o2 on ci1.order_id = o2.id\n" +
            "                                    join users u2 on u2.id = ci1.user_id\n" +
            "                                    where o2.id = o.id and ci1.user_id = u2.id and u2.id = :userId and ci1.for_charity = true),\n" +
            "\n" +
            "                                    (select sum(ci2.fixed_price * ci2.quantity) as myCart from cart_item ci2\n" +
            "                                    join orders o2 on ci2.order_id = o2.id\n" +
            "                                    join users u2 on u2.id = ci2.user_id where o2.id = o.id and ci2.user_id = u2.id and u2.id = :userId and ci2.for_charity = false),\n" +
            "\n" +
            "                                    sum(ci.quantity * ci.fixed_price) totalMoney from orders o\n" +
            "                                    join cart_item ci on o.id = ci.order_id\n" +
            "                                    join users u on ci.user_id = u.id\n" +
            "                                    where u.id =:userId  and o.user_id = u.id\n" +
            "                                    group by o.id;", nativeQuery = true)
    List<OrderProjection> getByUserId(UUID userId);

    @Query(nativeQuery = true,value = "select cast(o.id as varchar) as          id,\n" +
            "                   o.code,\n" +
            "                   o.address,\n" +
            "                   cast (o.created_at as date)                     createdAt,\n" +
            "                   o.latitude,\n" +
            "                   o.longitude,\n" +
            "                   o.phone_number        as          phoneNumber,\n" +
            "                   o.shipping_amount     as          shippingAmount,\n" +
            "            (select sum(ci1.fixed_price * ci1.quantity) as charityCart from cart_item ci1\n" +
            "            join orders o2 on ci1.order_id = o2.id\n" +
            "            where o2.id = o.id and ci1.for_charity = true),\n" +
            "\n" +
            "            (select sum(ci2.fixed_price * ci2.quantity) as myCart from cart_item ci2\n" +
            "            join orders o2 on ci2.order_id = o2.id\n" +
            "            where o2.id = o.id  and ci2.for_charity = false),\n" +
            "\n" +
            "            sum(ci.quantity * ci.fixed_price) totalMoney from orders o\n" +
            "            join cart_item ci on o.id = ci.order_id\n" +
            "\n" +
            "            group by o.id,o.code  order by o.code ")
    List<InterfaceOrderDto> findOrders();

    @Query(
            nativeQuery = true,
            value = "select coalesce(max(cast(substring(o.code,12) as int)),0) num\n" +
                    "                                        from orders o\n" +
                    "                                        where cast(o.created_at as date) = :date"
    )
    int findLastOrderCodeForDate(LocalDate date);
    boolean existsById(UUID id);

    @Query(value = "select cast(o.id as varchar) as          id,\n" +
            "                    o.code,\n" +
            "                    o.address,\n" +
            "                    cast (o.created_at as date)                     createdAt,\n" +
            "                    o.latitude,\n" +
            "                    o.longitude,\n" +
            "                    o.phone_number        as          phoneNumber,\n" +
            "                    o.shipping_amount     as          shippingAmount,\n" +
            "             (select cast(d.phone_number as varchar) as driverPhoneNumber from users d\n" +
            "             join orders o3 on d.id = o3.driver_id\n" +
            "             where o3.id = o.id),\n" +
            "             (select sum(ci1.fixed_price * ci1.quantity) as charityCart from cart_item ci1\n" +
            "             join orders o2 on ci1.order_id = o2.id\n" +
            "             join users u2 on u2.id = ci1.user_id\n" +
            "             where o2.id = o.id and ci1.user_id = u2.id and ci1.for_charity = true),\n" +
            "\n" +
            "             (select sum(ci2.fixed_price * ci2.quantity) as myCart from cart_item ci2\n" +
            "             join orders o2 on ci2.order_id = o2.id\n" +
            "             join users u2 on u2.id = ci2.user_id where o2.id = o.id and ci2.user_id = u2.id and ci2.for_charity = false),\n" +
            "\n" +
            "             sum(ci.quantity * ci.fixed_price) totalMoney from orders o\n" +
            "             join cart_item ci on o.id = ci.order_id\n" +
            "             join users u on o.driver_id = u.id\n" +
            "             join order_status os on os.id = o.order_status_id\n" +
            "             where u.id =:driverId and os.name='IN_PROGRESS'\n" +
            "             group by o.id",nativeQuery = true)
    List<InterfaceOrderDto> findByDriverId(UUID driverId);
    boolean existsByUserId(UUID user_id);
    boolean existsByDriverId(UUID driver_id);

    @Query(value = "select cast(o.id as varchar) as          id,\n" +
            "       o.code,\n" +
            "       cast (o.created_at as date)                     createdAt,\n" +
            "       o.shipping_amount     as          shippingAmount,\n" +
            "       os.name  orderStatusName\n" +
            "from orders o\n" +
            "         join cart_item ci on o.id = ci.order_id\n" +
            "         join users u on o.driver_id = u.id\n" +
            "         join order_status os on os.id = o.order_status_id\n" +
            "where u.id =:driverId and os.name<>'IN_PROGRESS'\n" +
            "group by o.id,os.name",nativeQuery = true)
    List<InterfaceDriverOrderDto> getByDriverId(UUID driverId);
}
