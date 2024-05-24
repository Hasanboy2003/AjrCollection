package com.yurakamri.ajrcollection.repository;

import com.yurakamri.ajrcollection.entity.CartItem;
import com.yurakamri.ajrcollection.payload.interfaceDto.InterfaceCartDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepo extends JpaRepository<CartItem, UUID> {

    List<CartItem> getAllByUserIdAndOrderIsNull(UUID user_id);
    List<CartItem>  findByUserIdAndOrderIsNullAndForCharityIsFalse(UUID user_id);
    List<CartItem> findByUserIdAndOrderIsNullAndForCharityIsTrue(UUID user_id);
    void deleteAllByUserIdAndOrderIsNull(UUID user_id);
    Optional<CartItem> findByUserIdAndSizeIdAndProductColorIdAndOrderIsNullAndForCharity(UUID user_id, Long size_id, UUID productColor_id, boolean forCharity);
    Optional<CartItem> findByIdAndUserId(UUID id, UUID user_id);

    @Query(nativeQuery = true, value = "select cast((\n" +
            "                (\n" +
            "                    select coalesce(sum(id.quantity), 0)\n" +
            "                    from income_detail id\n" +
            "                             join product_color pc on id.product_color_id = pc.id and pc.product_id = p.id\n" +
            "                             join income i on id.income_id = i.id\n" +
            "                             join color c on c.id = pc.color_id\n" +
            "                             join size s on id.size_id = s.id\n" +
            "                    where c.id = :colorId\n" +
            "                      and s.id = :sizeId\n" +
            "                )\n" +
            "                -\n" +
            "                (\n" +
            "                    select coalesce(sum(ci.quantity), 0)\n" +
            "                    from cart_item ci\n" +
            "                             join product_color pc on ci.product_color_id = pc.id and pc.product_id = p.id\n" +
            "                             join orders o on ci.order_id = o.id\n" +
            "                             join order_status os on os.id = o.order_status_id\n" +
            "                             join color c on pc.color_id = c.id\n" +
            "                             join size s on ci.size_id = s.id\n" +
            "                    where os.name <> 'REJECTED'\n" +
            "                      and c.id = :colorId\n" +
            "                      and s.id = :sizeId\n" +
            "                )\n" +
            "            ) as INTEGER)  quantity\n" +
            "        from product p\n" +
            "        where p.id = :productId")
    Integer findQuantityByProductIdAndColorIdAndSizeId(@Param("productId") UUID productId,@Param("colorId") Long colorId,@Param("sizeId") Long sizeId);

    List<InterfaceCartDto> findAllByOrderId(UUID order_id);

}
