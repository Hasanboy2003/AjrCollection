package com.yurakamri.ajrcollection.service;

import com.yurakamri.ajrcollection.entity.Brand;
import com.yurakamri.ajrcollection.entity.Size;
import com.yurakamri.ajrcollection.exception.PageSizeException;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.BrandDto;
import com.yurakamri.ajrcollection.payload.SizeDto;
import com.yurakamri.ajrcollection.repository.AttachmentRepository;
import com.yurakamri.ajrcollection.repository.BrandRepository;
import com.yurakamri.ajrcollection.repository.SizeRepository;
import com.yurakamri.ajrcollection.utills.CommandUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SizeService {

    private final SizeRepository sizeRepository;


    public ApiResponse getAllSize() {
            List<SizeDto> sizeDtoList = sizeRepository.findAll().stream().map(this::generateSizeDto).collect(Collectors.toList());
            if(sizeDtoList.isEmpty())
                return new ApiResponse(false,"Sizes do not exist");
            else
                return new ApiResponse(true,"All sizes",sizeDtoList);
    }

    SizeDto generateSizeDto(Size size){
        return new SizeDto(size.getId(),size.getName(),size.isActive());
    }

    public ApiResponse getByIdSize(Long id) {
        Size size = sizeRepository.findById(id).orElse(null);
        if(size!=null){
            return new ApiResponse(true,"Size by id",new SizeDto(size.getId(),size.getName(),size.isActive()));
        }
        return new ApiResponse(false,"Size does not exist");
    }

    public ApiResponse deleteSize(Long id) {
        Optional<Size> optionalSize = sizeRepository.findById(id);
        if (optionalSize.isEmpty()){
            return new ApiResponse();
        }
        try {
            sizeRepository.deleteById(id);
            return new ApiResponse(true, "O`chirildi", optionalSize.get());
        }catch (Exception e){
            return new ApiResponse(false, "Siz "+optionalSize.get().getName()+" ni " +
                    "o`chira olmaysiz.");
        }
    }

    public ApiResponse addSize(SizeDto dto) {
        if (sizeRepository.existsAllByName(dto.getName())){
            return new ApiResponse(false, "Siz kiritgan nomli Size mavjud");
        }
        Size size = new Size();
        size.setName(dto.getName());
        size.setActive(dto.isActive());
        sizeRepository.save(size);
        return new ApiResponse(true,"Size muvaffaqqiyatli qo'shildi", size);
    }

    public ApiResponse editSize(Long id, SizeDto dto) {
        Optional<Size> optionalSize = sizeRepository.findById(id);
        if (optionalSize.isEmpty()){
            return new ApiResponse(false, "Siz so`ragan Size mavjud emas");
        }
        Size size = optionalSize.get();
        size.setName(dto.getName());
        size.setActive(dto.isActive());
        try {
            sizeRepository.save(size);
            return new ApiResponse(true,"Size muvaffaqqiyatli o`zgartirildi");
        }catch (Exception e){
            return new ApiResponse(false, "Siz kiritgan nomli Size mavjud");
        }
    }

    public ApiResponse patch(Long id, boolean active) {
        if(!sizeRepository.existsById(id))
            return new ApiResponse(false,"Size does not exist!");
        Size size = sizeRepository.getById(id);
        size.setActive(active);
        sizeRepository.save(size);
        return new ApiResponse(true,active?"Size is activated":"Size is blocked!");
    }
}
