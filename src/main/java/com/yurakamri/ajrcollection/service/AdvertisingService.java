package com.yurakamri.ajrcollection.service;

import com.yurakamri.ajrcollection.entity.Advertising;
import com.yurakamri.ajrcollection.entity.Attachment;
import com.yurakamri.ajrcollection.payload.AdvertisingDto;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.repository.AdvertisingRepository;
import com.yurakamri.ajrcollection.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdvertisingService {


    private final AdvertisingRepository advertisingRepository;

    private final AttachmentRepository attachmentRepository;


    public ApiResponse getAllAdvertisingForAdmin() {
        List<Advertising> advertisingRepositoryAll = advertisingRepository.findAll();
        List<AdvertisingDto> advertisingDtoList=new ArrayList<>();
        for (Advertising advertising : advertisingRepositoryAll) {
            advertisingDtoList.add(generateAdvertisingDto(advertising));
        }
        return new ApiResponse(true, "ALL Advertising", advertisingDtoList);
    }


    public AdvertisingDto generateAdvertisingDto(Advertising advertising){
        return new AdvertisingDto(advertising.getId(), advertising.getAttachment().getId(),
                advertising.getTitle(), advertising.getDescription(), advertising.getDestination(),
                advertising.isActive());
    }

    public ApiResponse getByIdForAdmin(UUID id){
        Optional<Advertising> optionalAdvertising = advertisingRepository.findById(id);
        return optionalAdvertising.map(advertising -> new ApiResponse(true, "Advertising ", generateAdvertisingDto(advertising))).orElseGet(() -> new ApiResponse(false, "Advertising mavjud emas"));
    }

    public ApiResponse addAdvertising(AdvertisingDto advertisingDto){
        Advertising advertising=new Advertising(advertisingDto.getTitle(), advertisingDto.getDescription(),
                advertisingDto.getDestination(), advertisingDto.isActive());
        if (advertisingDto.getAttachmentId()!=null){
            Optional<Attachment> optionalAttachment = attachmentRepository.findById(advertisingDto.getAttachmentId());
            optionalAttachment.ifPresent(advertising::setAttachment);
        }
        advertisingRepository.save(advertising);
        return new ApiResponse(true, "Advertising saqlandi");
    }


    public ApiResponse editAdvertising(UUID id, AdvertisingDto advertisingDto){
        Optional<Advertising> optionalAdvertising = advertisingRepository.findById(id);
        if (optionalAdvertising.isEmpty()){
            return new ApiResponse(false, "Siz tanalagan Advertising mavjud emas");
        }

        Advertising advertising=optionalAdvertising.get();
        advertising.setTitle(advertisingDto.getTitle());
        advertising.setDescription(advertisingDto.getDescription());
        advertising.setDestination(advertisingDto.getDestination());
        advertising.setActive(advertisingDto.isActive());
        if (advertisingDto.getAttachmentId()!=null){
            Optional<Attachment> optionalAttachment = attachmentRepository.findById(advertisingDto.getAttachmentId());
            optionalAttachment.ifPresent(advertising::setAttachment);
        }
        advertisingRepository.save(advertising);
        return new ApiResponse(true, "Tahrirlandi");
    }


    public ApiResponse deleteAdvertising(UUID id) {
        boolean existsById = advertisingRepository.existsById(id);
        if (existsById) {
            advertisingRepository.deleteById(id);
            return new ApiResponse(true, "Advertising o`chirildi");
        }
        return new ApiResponse(false, "Advertising mavjud emas");
    }


    public ApiResponse enableOrDisable(UUID id, boolean active) {
        Optional<Advertising> optionalAdvertising = advertisingRepository.findById(id);
        if (optionalAdvertising.isEmpty()) {
            return new ApiResponse(false, "Advertising mavjud emas");
        }

        Advertising advertising = optionalAdvertising.get();
        if (advertising.isActive() == active) {
            if (active) return new ApiResponse(false, "Advertising avvaldan aktiv");
            return new ApiResponse(false, "Advertising avvaldan aktiv emas");
        }

        advertising.setActive(active);
        advertisingRepository.save(advertising);
        return new ApiResponse(true, "Advertising " + (active ? "aktivlashtirildi. " : "bloklandi"));
    }


    public ApiResponse getAllForUser(){
        List<Advertising> advertisingList = advertisingRepository.findAll();
        List<AdvertisingDto> advertisingDtoList=new ArrayList<>();
        for (Advertising advertising : advertisingList) {
            if (advertising.isActive()){
            advertisingDtoList.add(generateAdvertisingDto(advertising));
           }
        }
        return new ApiResponse(true, "Active Advertings", advertisingDtoList);
    }
}
