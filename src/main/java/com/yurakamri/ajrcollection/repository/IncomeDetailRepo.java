package com.yurakamri.ajrcollection.repository;

import com.yurakamri.ajrcollection.entity.IncomeDetail;
import com.yurakamri.ajrcollection.projection.IncomeDetailProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IncomeDetailRepo extends JpaRepository<IncomeDetail, UUID> {

    @Query(
            nativeQuery = true,
            value = "select vidp.*\n" +
                    "                    from view_income_details_projection vidp\n" +
                    "                    join income i on cast(i.id as varchar) = vidp.incomeId\n" +
                    "                    where i.id = :incomeId"
    )
    List<IncomeDetailProjection> getIncomeDetailsProjectionByIncomeId(@Param("incomeId") UUID incomeId);

    @Query(
            nativeQuery = true,
            value = "select * from view_income_details_projection vidp\n" +
                    "                    where vidp.id = cast(:incomeDetailId as varchar)"
    )
    IncomeDetailProjection getIncomeDetailProjectionById(@Param("incomeDetailId") UUID incomeDetailId);

    @Query(
            nativeQuery = true,
            value = "select * from view_income_details_projection"
    )
    List<IncomeDetailProjection> getIncomeDetailsProjection(Pageable pageable);

    @Query(
            nativeQuery = true,
            value = "select *\n" +
                    "                    from view_income_details_projection vidp\n" +
                    "                    where vidp.id = cast(:id as varchar)"
    )
    Optional<IncomeDetailProjection> getIncomeDetailProjectionByIdOptional(@Param("id") UUID id);


    List<IncomeDetail> findAllByProductColorId(UUID productColor_id);


//    @Query(
//            nativeQuery = true,
//            value = "select  size_id from income_details where product_color_id=:id"
//    )
//    Set<Long> getAllByProductColorId(UUID productColorId);




}
