package com.yurakamri.ajrcollection.mapper;

import com.yurakamri.ajrcollection.entity.Income;
import com.yurakamri.ajrcollection.entity.IncomeDetail;
import com.yurakamri.ajrcollection.entity.ProductColor;
import com.yurakamri.ajrcollection.entity.Size;
import com.yurakamri.ajrcollection.exception.ResourceNotFoundException;
import com.yurakamri.ajrcollection.mapper.base.BaseMapper;
import com.yurakamri.ajrcollection.payload.req.incomeDetail.IncomeDetailCreateDto;
import com.yurakamri.ajrcollection.repository.IncomeRepo;
import com.yurakamri.ajrcollection.repository.ProductColorRepository;
import com.yurakamri.ajrcollection.repository.SizeRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@RequiredArgsConstructor
@Mapper(componentModel = "spring")
public abstract class IncomeDetailMapper implements BaseMapper<IncomeDetail, IncomeDetailCreateDto, IncomeDetailCreateDto> {

    @Autowired
    protected SizeRepository sizeRepo;

    @Autowired
    protected ProductColorRepository productColorRepo;

    @Autowired
    protected IncomeRepo incomeRepo;

    @Mappings(
            value = {
                    @Mapping(expression = "java(getIncome(src.getIncomeId()))", target = "income"),
                    @Mapping(expression = "java(getProductColor(src.getProductId(), src.getColorId()))", target = "productColor"),
                    @Mapping(expression = "java(getSize(src.getSizeId()))", target = "size")
            }
    )
    @Override
    public abstract IncomeDetail fromCreateDto(IncomeDetailCreateDto src);

    @Mappings(
            value = {
                    @Mapping(expression = "java(getProductColor(src.getProductId(), src.getColorId()))", target = "productColor"),
                    @Mapping(expression = "java(getSize(src.getSizeId()))", target = "size"),
                    @Mapping(source = "quantity", target = "quantity"),
                    @Mapping(source = "price", target = "price")
            }
    )
    @Override
    public abstract void fromUpdateDto(IncomeDetailCreateDto src, @MappingTarget IncomeDetail target);

    @Named("getIncome")
    public Income getIncome(UUID incomeId) {
        if (incomeId == null) throw new NullPointerException("INCOME ID OF INCOME DETAIL MUST NOT BE NULL");
        return incomeRepo
                .findById(incomeId)
                .orElseThrow(() -> new ResourceNotFoundException(incomeId, "INCOME"));
    }

    @Named("getProductColor")
    public ProductColor getProductColor(UUID productId, Long colorId) {
        return productColorRepo
                .findByProductIdAndColorId(productId, colorId)
                .orElseThrow(() -> new NullPointerException(
                        "PRODUCT COLOR WITH PRODUCT ID: " + productId + " AND COLOR ID: " + colorId + " NOT FOUND!"));
    }

    @Named("getSize")
    public Size getSize(Long sizeId) {
        return sizeRepo
                .findById(sizeId)
                .orElseThrow(() -> new ResourceNotFoundException(sizeId, "SIZE"));
    }
}
