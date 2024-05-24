package com.yurakamri.ajrcollection.repository;

import com.yurakamri.ajrcollection.entity.Product;
import com.yurakamri.ajrcollection.entity.ProductColor;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.product.ProductDtoForSales;
import com.yurakamri.ajrcollection.projection.ProductColorProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductColorRepository extends JpaRepository<ProductColor, UUID> {

    Optional<ProductColor> findByProductIdAndColorId(UUID product_id, Long color_id);

    @Query(
            nativeQuery = true,
            value = "select vpcp.*\n" +
                    "                    from view_product_colors_projection vpcp\n" +
                    "                    join income_detail id2 on cast(id2.product_color_id as varchar) = vpcp.id\n" +
                    "                    where id2.id = :incomeDetailId"
    )
    ProductColorProjection getProductColorProjectionByIncomeDetailId(@Param("incomeDetailId") UUID incomeDetailId);

    Optional<ProductColor> findByColorIdAndProductId(Long color_id, UUID product_id);
    ProductColor getAllByProductId(UUID product_id);

    List<ProductColor> findAllByProductId(UUID id);


}
