package com.yurakamri.ajrcollection.service;

import com.yurakamri.ajrcollection.entity.Brand;
import com.yurakamri.ajrcollection.exception.PageSizeException;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.BrandDto;
import com.yurakamri.ajrcollection.repository.AttachmentRepository;
import com.yurakamri.ajrcollection.repository.BrandRepository;
import com.yurakamri.ajrcollection.utills.CommandUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    private final AttachmentRepository attachmentRepository;


    public ApiResponse getAllBrand(int page, int size) {
        try {
            Pageable pageable = CommandUtils.descOrAscByCreatedAtPageable(page, size, true);
            Page<Brand> brands = brandRepository.findAll(pageable);
            if(brands.isEmpty())
                return new ApiResponse(false,"Brands do not exist");
            List<BrandDto> brandDtoList=new ArrayList<>();
            for (Brand brand : brands) {
                brandDtoList.add(generateBrandDtoForAdmin(brand));
            }
            return new ApiResponse(true,"All brands",brandDtoList);
        } catch (PageSizeException e) {
            return new ApiResponse(false,e.getMessage());
        }
    }

    public BrandDto generateBrandDtoForAdmin(Brand brand){
        return new BrandDto(brand.getId(), brand.getName(), brand.isActive(),
                brand.getLogo()!=null?brand.getLogo().getId():null, Timestamp.valueOf(brand.getCreatedAt()));
    }

    public ApiResponse getByIdBrand(Long id) {
        Optional<Brand> optionalBrand = brandRepository.findById(id);
        return optionalBrand.map(brand -> new ApiResponse(true, "Brand ", generateBrandDtoForAdmin(brand))).orElseGet(() -> new ApiResponse(false, "Brand mavjud emas"));
    }

    public ApiResponse deleteBrand(Long id) {
        Optional<Brand> optionalBrand = brandRepository.findById(id);
        if (optionalBrand.isEmpty()){
            return new ApiResponse(false,"Siz so`ragan Brand mavjud emas");
        }
        try {
            brandRepository.deleteById(id);
            return new ApiResponse(true, "O`chirildi", optionalBrand.get());
        }catch (Exception e){
            return new ApiResponse(false, "Siz "+optionalBrand.get().getName()+" " +
                    "o`chira olmaysiz. Uni aktivligini o`chirib qo`yishingiz mumkin. ");
        }
    }

    public ApiResponse addBrand(BrandDto brandDto) {
        if (brandRepository.existsAllByName(brandDto.getName())){
            return new ApiResponse(false, "Siz kiritgan nomli Brand mavjud");
        }
        Brand brand=new Brand(brandDto.getName(), brandDto.isActive());
        if (brandDto.getLogoId()!=null){
            brand.setLogo(attachmentRepository.findById(brandDto.getLogoId()).orElse(null));
        }
        brand = brandRepository.save(brand);
        return new ApiResponse(true,"Brand muvaffaqqiyatli qo`shildi");
    }

    public ApiResponse editBrand(BrandDto brandDto) {
        Optional<Brand> optionalBrand = brandRepository.findById(brandDto.getId());
        if (optionalBrand.isEmpty()){
            return new ApiResponse(false, "Siz so`ragan Brand mavjud emas");
        }
        Brand brand=optionalBrand.get();
        brand.setName(brandDto.getName());
        brand.setActive(brandDto.isActive());
        if (brandDto.getLogoId()!=null){
            brand.setLogo(attachmentRepository.findById(brandDto.getLogoId()).orElse(null));
        }
        try {
            brandRepository.save(brand);
            return new ApiResponse(true,"Brand muvaffaqqiyatli o`rgartirildi");
        }catch (Exception e){
            return new ApiResponse(false, "Siz kiritgan nomli Brand mavjud");
        }
    }

    public ApiResponse enabledOrDisabled(Long id, boolean isActive) {
        Optional<Brand> optionalBrand = brandRepository.findById(id);
        if (optionalBrand.isPresent()){
            optionalBrand.get().setActive(isActive);
            brandRepository.save(optionalBrand.get());
            return new ApiResponse(true, isActive?"Brand aktivlashtirishdi":
                    "Brand aktivligi o`chirildi");
        }
        return new ApiResponse(false, "Siz tanalagan Brand mavjud emas");
    }


}
