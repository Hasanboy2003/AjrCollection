package com.yurakamri.ajrcollection.service;

import com.yurakamri.ajrcollection.entity.Distributor;
import com.yurakamri.ajrcollection.exception.PageSizeException;
import com.yurakamri.ajrcollection.exception.ResourceNotFoundException;
import com.yurakamri.ajrcollection.mapper.DistributorMapper;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.req.DistributorDto;
import com.yurakamri.ajrcollection.projection.distributor.DistributorProjection;
import com.yurakamri.ajrcollection.repository.DistributorRepo;
import com.yurakamri.ajrcollection.utills.CommandUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static com.yurakamri.ajrcollection.payload.ApiResponse.response;

@Service
@Transactional
@RequiredArgsConstructor
public class DistributorService {

    private final DistributorRepo distributorRepo;
    private final DistributorMapper mapper;

    public ResponseEntity<ApiResponse> getDistributors(Integer page) throws PageSizeException {
        Pageable pageable = CommandUtils.simplePageable(page, 20, Sort.Direction.ASC, "name");
        return response(HttpStatus.OK, distributorRepo.getDistributorsProjections(pageable));
    }

    public ResponseEntity<ApiResponse> getById(Long id) {
        Objects.requireNonNull(id);
        Optional<DistributorProjection> optional = distributorRepo.getDistributorProjectionByIdOptional(id);
        return optional.map(ApiResponse::response).orElseThrow(() -> new ResourceNotFoundException(id, "DISTRIBUTOR"));
    }

    public ResponseEntity<ApiResponse> addDistributor(DistributorDto dto) {
        if (distributorRepo.existsByName(dto.getName())) {
            return response(false, "DISTRIBUTOR WITH NAME : " + dto.getName() + " ALREADY EXISTS!");
        }
        Distributor saved = distributorRepo.save(new Distributor(dto.getName(), dto.getDescription(), dto.isActive()));
        return distributorRepo
                .getDistributorProjectionByIdOptional(saved.getId())
                .map(ApiResponse::response)
                .orElseThrow(() -> new ResourceNotFoundException(saved.getId(), "DISTRIBUTOR"));
    }

    public ResponseEntity<ApiResponse> deleteDistributor(Long id) {
        try {
            if (distributorRepo.existsById(id)) distributorRepo.deleteById(id);
            else return response(false, "DISTRIBUTOR WITH ID : " + id + " NOT FOUND!");
        } catch (Exception e) {
            return response(false, "DISTRIBUTOR CANNOT BE DELETED!");
        }
        return response(HttpStatus.OK, "Distributor deleted!");
    }

    public ResponseEntity<ApiResponse> updateDistributor(Long id, DistributorDto dto) {
        Distributor distributor = distributorRepo
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "DISTRIBUTOR"));

        mapper.updateDistributorFromDistributorDto(dto, distributor);
        try {
            distributor = distributorRepo.save(distributor);
            return response("Distributor updated!", distributorRepo.getDistributorProjectionById(distributor.getId()));
        } catch (Exception e) {
            return response(false, "DISTRIBUTOR COULD NOT BE UPDATED!", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<ApiResponse> patch(Long id, boolean active) {
        if(!distributorRepo.existsById(id))
             return response(false, "DISTRIBUTOR WITH ID : " + id + " NOT FOUND!");
        Distributor distributor = distributorRepo.getById(id);
        distributor.setActive(active);
        distributorRepo.save(distributor);
        return response(true,active?"DISTRIBUTOR IS ACTIVATED":"DISTRIBUTOR IS BLOCKED");
    }
}
