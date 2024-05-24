package com.yurakamri.ajrcollection.repository;

import com.yurakamri.ajrcollection.entity.Income;
import com.yurakamri.ajrcollection.projection.income.IncomeProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IncomeRepo extends JpaRepository<Income, UUID> {
    @Query(value = "select count(id) from income",nativeQuery = true)
    long countIncomes();
    /**
     * INCOMES INFORMATION CONSIDERING INCOME DETAILS
     */
    @Query(
            nativeQuery = true,
            value = "select * from view_incomes_projection"
    )
    List<IncomeProjection> getIncomesProjectionPageable(Pageable pageable);

    /**
     * TO GET THE MAX INCOME CODE SO THAT THE NEXT CODE WILL BE BIGGER
     */
    @Query(
            nativeQuery = true,
            value = "select coalesce(max(cast(substring(i.code,14) as bigint)), 0)\n" +
                    "from income i\n" +
                    "where cast(i.created_at as date) = :date"
    )
    Long getLastIncomeCodeForDate(@Param("date") LocalDate date);

    /**
     * ONLY INCOME INFORMATION
     */
    @Query(
            nativeQuery = true,
            value = "select * from view_incomes_projection vip where vip.id = cast(:id as varchar)"
    )
    Optional<IncomeProjection> getIncomeProjectionOptional(@Param("id") UUID id);

    /**
     * GET SINGLE INCOME INFORMATION
     */
    @Query(
            nativeQuery = true,
            value = "select * from view_incomes_projection vip where vip.id = cast(:id as varchar)"
    )
    IncomeProjection getIncomeProjection(@Param("id") UUID id);



}
