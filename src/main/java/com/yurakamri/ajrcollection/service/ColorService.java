package com.yurakamri.ajrcollection.service;

import com.yurakamri.ajrcollection.entity.Color;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.ColorDto;
import com.yurakamri.ajrcollection.repository.ColorRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColorService {

    private final ColorRepo colorRepo;

    public ApiResponse getColors() {
        List<ColorDto> collect = colorRepo.findAllByOrderByActiveDesc().stream().map(this::generateDto).collect(Collectors.toList());
        return new ApiResponse(true, "All colors", collect);
    }

    public ColorDto generateDto(Color color) {
        return new ColorDto(color.getId(), color.getName(), color.getCode(), color.isActive());
    }

    public ApiResponse getById(Long id) {
        if (!colorRepo.existsById(id))
            return new ApiResponse(false, "Color doesn't exist");
        return new ApiResponse(true, "Color is found", generateDto(colorRepo.getById(id)));
    }

    public ApiResponse delete(Long id) {
        if (!colorRepo.existsById(id))
            return new ApiResponse(false, "Color doesn't exist");
        try {
            colorRepo.deleteById(id);
            return new ApiResponse(true, "Color is deleted");
        } catch (Exception e) {
            return new ApiResponse(false, "Color can not be deleted. You can block it");
        }
    }

    public ApiResponse update(ColorDto dto) {
        Optional<Color> optionalColor = colorRepo.findById(dto.getId());
        if (optionalColor.isEmpty())
            return new ApiResponse(false, "Color doesn't exist");
        Color color = optionalColor.get();
        if(!colorRepo.isNotAvailable(color.getId(), color.getName(), color.getCode())){
        return new ApiResponse(false,"Name or code already exists!");
        }
        color.setName(dto.getName());
        color.setCode(dto.getCode());
        color.setActive(dto.isActive());
        colorRepo.save(color);
        return new ApiResponse(true, "Color is updated");
    }

    public ApiResponse create(ColorDto dto) {
        if(colorRepo.existsByName(dto.getName()))
            return new ApiResponse(false,"Color name already exist!");
        if(colorRepo.existsByCode(dto.getCode()))
            return new ApiResponse(false,"Color code already exist!");
        Color color = new Color();
        color.setActive(dto.isActive());
        color.setName(dto.getName());
        color.setCode(dto.getCode());
        color.setActive(dto.isActive());
        colorRepo.save(color);
        return new ApiResponse(true, "Color created");
    }

    public ApiResponse patch(Long id, boolean active) {
        if(!colorRepo.existsById(id))
            return new ApiResponse(false,"Color does not exist!");
        Color color = colorRepo.getById(id);
        color.setActive(active);
        colorRepo.save(color);
        return new ApiResponse(true,active?"Color is activated":"Color is blocked!");
    }
}
