package com.yurakamri.ajrcollection.repository;

import com.yurakamri.ajrcollection.entity.Discount;
import com.yurakamri.ajrcollection.projection.DiscountProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiscountRepo extends JpaRepository<Discount, Long> {

    /**
     * GET SINGLE DISCOUNT PROJECTION BY ID
     */
    @Query(
            nativeQuery = true,
            value = "select *\n" +
                    "                    from view_discounts_projection vdp\n" +
                    "                    where vdp.id = :id"
    )
    DiscountProjection getDiscountProjectionById(@Param("id") Long id);

    /**
     * GET DISCOUNTS PROJECTION PAGEABLE
     */
    @Query(
            nativeQuery = true,
            value = "select *\n" +
                    "                    from view_discounts_projection"
    )
    List<DiscountProjection> getDiscountsProjection();
}
