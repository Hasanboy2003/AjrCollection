package com.yurakamri.ajrcollection.repository;

import com.yurakamri.ajrcollection.entity.Brand;
import com.yurakamri.ajrcollection.projection.brand.BrandProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    boolean existsAllByName(String name);

    @Query(
            nativeQuery = true,
            value = "select prj.*\n" +
                    "                    from view_brands_projection prj\n" +
                    "                    join product p on p.brand_id = prj.id\n" +
                    "                    where p.id = :productId"
    )
    BrandProjection getBrandProjectionByProductId(@Param("productId") UUID productId);
}
