package com.yurakamri.ajrcollection.mapper;

import com.yurakamri.ajrcollection.entity.Distributor;
import com.yurakamri.ajrcollection.payload.req.DistributorDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface DistributorMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDistributorFromDistributorDto(DistributorDto dto, @MappingTarget Distributor distributor);
}
