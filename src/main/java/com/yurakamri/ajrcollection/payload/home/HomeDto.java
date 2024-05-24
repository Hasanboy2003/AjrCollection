package com.yurakamri.ajrcollection.payload.home;

import com.yurakamri.ajrcollection.payload.AdvertisingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeDto {



    private List<AdvertisingDto> advertisingDtoList;


    private List<BrandDtoForHome> brandDtoForHomeSet;

    private List<BrandProductDto> brandProductDto;


}
