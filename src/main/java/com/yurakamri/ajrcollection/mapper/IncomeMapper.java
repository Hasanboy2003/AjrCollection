package com.yurakamri.ajrcollection.mapper;

import com.yurakamri.ajrcollection.entity.Distributor;
import com.yurakamri.ajrcollection.entity.Income;
import com.yurakamri.ajrcollection.exception.ResourceNotFoundException;
import com.yurakamri.ajrcollection.mapper.base.BaseMapper;
import com.yurakamri.ajrcollection.payload.req.income.IncomeCreateDto;
import com.yurakamri.ajrcollection.payload.req.income.IncomeUpdateDto;
import com.yurakamri.ajrcollection.repository.DistributorRepo;
import com.yurakamri.ajrcollection.repository.IncomeRepo;
import lombok.RequiredArgsConstructor;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.yurakamri.ajrcollection.utills.AppConstants.BASE_INCOME_CODE_PREFIX;

@Mapper(componentModel = "spring")
@RequiredArgsConstructor
public abstract class IncomeMapper implements BaseMapper<Income, IncomeUpdateDto, IncomeCreateDto> {



    @Autowired
    protected DistributorRepo distributorRepo;

    @Autowired
    protected IncomeRepo incomeRepo;

    @Mappings(
            value = {
                    @Mapping(target = "distributor", expression = "java(getDistributor(src.getDistributorId()))"),
                    @Mapping(target = "code", expression = "java(getCode())")
            }
    )
    @Override
    public abstract Income fromCreateDto(IncomeCreateDto src);

    @Mappings(
            value = {
                    @Mapping(expression = "java(getDistributor(src.getDistributorId()))", target = "distributor"),
                    @Mapping(source = "description", target = "description")
            }
    )
    @Override
    public abstract void fromUpdateDto(IncomeUpdateDto src, @MappingTarget Income target);

    @Named("getDistributor")
    public Distributor getDistributor(Long id) {
        return distributorRepo
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "DISTRIBUTOR"));
    }

    @Named("getCode")
    public String getCode() {
        LocalDate forWhichDate = LocalDate.now();
        long nextIncomeCodeForDate = incomeRepo.getLastIncomeCodeForDate(forWhichDate) + 1;
        return BASE_INCOME_CODE_PREFIX +
                forWhichDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                "0".repeat(6 - String.valueOf(nextIncomeCodeForDate).length()) +
                nextIncomeCodeForDate;
    }
}
