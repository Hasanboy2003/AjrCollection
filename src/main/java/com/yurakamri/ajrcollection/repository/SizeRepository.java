package com.yurakamri.ajrcollection.repository;

import com.yurakamri.ajrcollection.entity.Size;
import com.yurakamri.ajrcollection.payload.interfaceDto.InterfaceSizeDto;
import com.yurakamri.ajrcollection.projection.SizeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SizeRepository extends JpaRepository<Size, Long> {

    boolean existsAllByName(String name);

    @Query(value = "select s.name from size s join cart_item ci on ci.size_id = s.id where ci.id=:cartId", nativeQuery = true)
    String findByCartItemDto(UUID cartId);

    @Query(
            nativeQuery = true,
            value = "select vsp.*\n" +
                    "                    from view_sizes_projection vsp\n" +
                    "                    join income_detail id2 on id2.size_id = vsp.id\n" +
                    "                    where id2.id = :incomeDetailId"
    )
    SizeProjection getSizeProjectionByIncomeDetailId(@Param("incomeDetailId") UUID incomeDetailId);

    List<Size> findAllByActiveIsTrue();

    Size getByName(String name);
}
