package com.yurakamri.ajrcollection.mapper;

import com.yurakamri.ajrcollection.entity.Discount;
import com.yurakamri.ajrcollection.entity.Product;
import com.yurakamri.ajrcollection.exception.ResourceNotFoundException;
import com.yurakamri.ajrcollection.mapper.base.BaseMapper;
import com.yurakamri.ajrcollection.payload.product.dicount.DiscountCreateDto;
import com.yurakamri.ajrcollection.payload.product.dicount.DiscountUpdateDto;
import com.yurakamri.ajrcollection.repository.ProductRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class DiscountMapper implements BaseMapper<Discount, DiscountUpdateDto, DiscountCreateDto> {

    @Autowired
    protected ProductRepository productRepo;

    @Mapping(expression = "java(getProductList(src.getProductIdList()))", target = "products")
    @Override
    public abstract Discount fromCreateDto(DiscountCreateDto src);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Override
    public abstract void fromUpdateDto(DiscountUpdateDto src, @MappingTarget Discount target);

    @Named("getProductList")
    public List<Product> getProductList(List<UUID> productIdList) {
        if (productIdList == null) return null;
        return productIdList
                .stream()
                .map(id -> productRepo
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(id, "PRODUCT")))
                .collect(Collectors.toList());
    }
}
