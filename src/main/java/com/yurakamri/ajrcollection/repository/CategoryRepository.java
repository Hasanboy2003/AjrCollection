package com.yurakamri.ajrcollection.repository;

import com.yurakamri.ajrcollection.entity.Category;
import com.yurakamri.ajrcollection.projection.category.CategoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existsAllByName(String name);

    @Query(
            nativeQuery = true,
            value = "select vcp.*\n" +
                    "                    from view_categories_projection vcp\n" +
                    "                    join product p on cast(p.category_id as varchar) = vcp.id\n" +
                    "                    where p.id = :productId"
    )
    CategoryProjection getCategoryProjectionByProductId(@Param("productId") UUID productId);

    CategoryProjection getCategoryProjectionById(@Param("id") UUID id);

    Category getByName(String name);

    @Query(nativeQuery = true,
    value = "select distinct c.* from category c join product p on c.id = p.category_id where p.brand_id=:brandId")
    List<Category> getAllByBrandId(@RequestParam("brandId") Long brandId);


}
