package com.yurakamri.ajrcollection.projection.income;

import com.yurakamri.ajrcollection.projection.IncomeDetailProjection;
import com.yurakamri.ajrcollection.projection.base.BaseGenericProjection;
import com.yurakamri.ajrcollection.projection.distributor.DistributorProjection;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface IncomeProjection extends BaseGenericProjection<UUID> {

    String getIncomeCode();

    String getIncomeDescription();

    BigDecimal getIncomeTotalSum();

    @Value("#{@distributorRepo.getDistributorProjectionByIncomeId(target.id)}")
    DistributorProjection getDistributor();

    @Value("#{@incomeDetailRepo.getIncomeDetailsProjectionByIncomeId(target.id)}")
    List<IncomeDetailProjection> getIncomesDetails();
}
