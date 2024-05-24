package com.yurakamri.ajrcollection.repository;

import com.yurakamri.ajrcollection.entity.Color;
import com.yurakamri.ajrcollection.payload.interfaceDto.InterfaceColorDto;
import com.yurakamri.ajrcollection.projection.ColorProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ColorRepo extends JpaRepository<Color, Long> {
    List<Color> findAllByOrderByActiveDesc();
    @Query(
            nativeQuery = true,
            value = "select vcp.*\n" +
                    "                    from view_colors_projection vcp\n" +
                    "                    join product_color pc on pc.color_id = vcp.id\n" +
                    "                    where pc.id = :productColorId"
    )
    ColorProjection getColorProjectionByProductColorId(@Param("productColorId") UUID productColorId);

    @Query(nativeQuery = true, value = "select case when id<>:Id and (name=:colorName or code=:code) then true " +
            "    else false end as myCase from color order by myCase desc limit 1;")
    boolean isNotAvailable(Long Id, String colorName, String code);

    boolean existsByName(String name);

    boolean existsByCode(String code);

    @Query(nativeQuery = true, value = "select c.code from color c\n" +
            "join product_color pc on c.id = pc.color_id\n" +
            "join cart_item ci on pc.id = ci.product_color_id\n" +
            "where ci.id=:cartId")
    String findByCartItemId(UUID cartId);
}
