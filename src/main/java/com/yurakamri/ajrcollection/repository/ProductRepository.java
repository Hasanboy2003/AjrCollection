package com.yurakamri.ajrcollection.repository;

import com.yurakamri.ajrcollection.entity.Product;
import com.yurakamri.ajrcollection.payload.interfaceDto.InterfaceProductDto;
import com.yurakamri.ajrcollection.projection.ProductProjection;
import com.yurakamri.ajrcollection.projection.ViewProductSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query(value = "select count(id) from product",nativeQuery = true)
    long countProducts();

    boolean existsAllByName(String name);

    @Query(value = "select code from product where created_at =(SELECT MAX(created_at) FROM product);", nativeQuery = true)
    Optional<String> byCodeFromNativeQuery();

    boolean existsAllByCode(String code);

    @Query(
            nativeQuery = true,
            value = "select vpp.*\n" +
                    "                    from view_products_projection vpp\n" +
                    "                    join product_color pc on cast(pc.product_id as varchar) = vpp.id\n" +
                    "                    where pc.id = :productColorId"
    )
    ProductProjection getProductProjectionByProductColorId(@Param("productColorId") UUID productColorId);

    @Query(
            nativeQuery = true,
            value = "select vpp.*\n" +
                    "                    from view_products_projection vpp\n" +
                    "                    join discount d on d.id = vpp.discountid\n" +
                    "                    where d.id = :discountId"
    )
    List<ProductProjection> getProductsProjectionByDiscountId(@Param("discountId") Long discountId);

    //DO NOT TOUCH
    @Query(nativeQuery = true,
            value = "select cast(p.id as varchar)             as id,\n" +
                    "                                               p.name,\n" +
                    "                                               b.name                            brandName,\n" +
                    "                                               c.name                            categoryName,\n" +
                    "                                               cast(pa.attachment_id as varchar) attachmentId\n" +
                    "                                        from product p\n" +
                    "                                                 join product_color pc on pc.product_id = p.id\n" +
                    "                                                 join product_attachment pa on pa.product_color_id = pc.id\n" +
                    "                                                 join attachment a on a.id = pa.attachment_id\n" +
                    "                                                 join brand b on b.id = p.brand_id\n" +
                    "                                                 join category c on p.category_id = c.id\n" +
                    "                                                 join cart_item ci on ci.product_color_id = pc.id\n" +
                    "                                        where ci.id = :cartId")
    InterfaceProductDto findByCartItemId(UUID cartId);

    @Query(nativeQuery = true, value = "select (\n" +
            "               (\n" +
            "                   select sum(id.quantity)\n" +
            "                   from income_detail id\n" +
            "                            join product_color pc on id.product_color_id = pc.id and pc.product_id = p.id\n" +
            "                            join income i on id.income_id = i.id\n" +
            "                            join color c on c.id = pc.color_id\n" +
            "                            join size s on id.size_id = s.id\n" +
            "               )\n" +
            "               -\n" +
            "               (\n" +
            "                   select sum(ci.quantity)\n" +
            "                   from cart_item ci\n" +
            "                            join product_color pc on ci.product_color_id = pc.id and pc.product_id = p.id\n" +
            "                            join orders o on ci.order_id = o.id\n" +
            "                            join order_status os on os.id = o.order_status_id\n" +
            "                            join color c on pc.color_id = c.id\n" +
            "                            join size s on ci.size_id = s.id\n" +
            "                   where os.name <> 'REJECTED'\n" +
            "               )\n" +
            "           ) as quantity\n" +
            "from product p\n" +
            "where p.id =:productId;")
    Integer getQuantityOfProduct(UUID productId);


    Page<Product> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Product> findAllByBrandIdAndCategoryIdOrCategory_ParentCategoryIdOrderByCreatedAtDesc(Long brand_id, UUID category_id, UUID category_parentCategory_id, Pageable pageable);

    Page<Product> findAllByBrandIdOrderByCreatedAtDesc(Long brand_id, Pageable pageable);

    Page<Product> findAllByCategoryIdOrCategory_ParentCategoryIdOrderByCreatedAtDesc(UUID categoryId, UUID category_parentCategory_id, Pageable pageable);

    @Query(
            nativeQuery = true,
            value = "SELECT cast(p.id AS varchar)                                AS id,\n" +
                    "       p.name                                               AS name,\n" +
                    "       cast((\n" +
                    "           SELECT pa.attachment_id\n" +
                    "           FROM product_color pc\n" +
                    "                    JOIN product p2 on p2.id = pc.product_id\n" +
                    "                    JOIN product_attachment pa on pc.id = pa.product_color_id\n" +
                    "           WHERE p2.id = p.id\n" +
                    "           order by pa.created_at desc\n" +
                    "           limit 1\n" +
                    "       ) AS varchar)                                        AS attachmentId,\n" +
                    "       cast(p.outcome_price as numeric(19, 2))              AS price,\n" +
                    "       case\n" +
                    "           when d.id IS NOT NULL and d.end_date >= cast(now() as date) then cast(d.percent as numeric(19, 2))\n" +
                    "           end                                              as discount,\n" +
                    "       case\n" +
                    "           when d.id IS NOT NULL and d.end_date >= cast(now() as date)\n" +
                    "               then cast((p.outcome_price - (p.outcome_price * d.percent / 100)) as numeric(19, 2))\n" +
                    "           else cast(p.outcome_price as numeric(19, 2)) end as sellingPrice,\n" +
                    "       b.name                                               as brandName,\n" +
                    "       case\n" +
                    "           when p.id in (\n" +
                    "               select uf.product_id\n" +
                    "               from users_favourites uf\n" +
                    "               WHERE cast(uf.user_id as varchar) = cast(:userId as varchar)\n" +
                    "           ) then true\n" +
                    "           else false end                                   as favourite\n" +
                    "from product p\n" +
                    "         JOIN category c on p.category_id = c.id\n" +
                    "         JOIN brand b on p.brand_id = b.id\n" +
                    "         left JOIN discount d on p.discount_id = d.id\n" +
                    "WHERE p.active=true and lower(p.name) like concat('%', lower(:searchText), '%')\n" +
                    "   or p.code = :searchText\n" +
                    "   or lower(b.name) like concat('%', lower(:searchText), '%')\n" +
                    "   or lower(c.name) like concat('%', lower(:searchText), '%')"
    )
    List<ViewProductSearch> searchProducts(@Param("searchText") String searchText,
                                           @Param("userId") UUID userId,
                                           Pageable pageable);
}
