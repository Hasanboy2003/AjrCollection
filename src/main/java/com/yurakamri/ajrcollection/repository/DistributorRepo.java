package com.yurakamri.ajrcollection.repository;

import com.yurakamri.ajrcollection.entity.Distributor;
import com.yurakamri.ajrcollection.projection.distributor.DistributorProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DistributorRepo extends JpaRepository<Distributor, Long> {

    boolean existsByName(String name);

    /**
     * GET DISTRIBUTOR INFORMATION BY ID OPTIONAL
     */
    @Query(
            nativeQuery = true,
            value = "select *\n" +
                    "                    from view_distributors_projection gdp\n" +
                    "                    where gdp.id = :id"
    )
    Optional<DistributorProjection> getDistributorProjectionByIdOptional(@Param("id") Long id);

    /**
     * GET DISTRIBUTOR INFORMATION BY ID
     */
    @Query(
            nativeQuery = true,
            value = "select *\n" +
                    "                    from view_distributors_projection gdp\n" +
                    "                    where gdp.id = :id"
    )
    DistributorProjection getDistributorProjectionById(@Param("id") Long id);

    /**
     * GET DISTRIBUTORS INFORMATION
     */
    @Query(
            nativeQuery = true,
            value = "select * from view_distributors_projection"
    )
    List<DistributorProjection> getDistributorsProjections(Pageable pageable);

    @Query(
            nativeQuery = true,
            value = "select vdp.*\n" +
                    "                    from view_distributors_projection vdp\n" +
                    "                    join income i on i.distributor_id = vdp.id\n" +
                    "                    where i.id = :incomeId"
    )
    DistributorProjection getDistributorProjectionByIncomeId(@Param("incomeId") UUID incomeId);
}
