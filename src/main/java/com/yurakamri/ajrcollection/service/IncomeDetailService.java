package com.yurakamri.ajrcollection.service;

import com.yurakamri.ajrcollection.entity.Income;
import com.yurakamri.ajrcollection.entity.IncomeDetail;
import com.yurakamri.ajrcollection.exception.PageSizeException;
import com.yurakamri.ajrcollection.exception.ResourceNotFoundException;
import com.yurakamri.ajrcollection.mapper.IncomeDetailMapper;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.req.incomeDetail.IncomeDetailCreateDto;
import com.yurakamri.ajrcollection.projection.IncomeDetailProjection;
import com.yurakamri.ajrcollection.repository.IncomeDetailRepo;
import com.yurakamri.ajrcollection.repository.IncomeRepo;
import com.yurakamri.ajrcollection.utills.CommandUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.yurakamri.ajrcollection.payload.ApiResponse.response;

@Service
@Transactional
@RequiredArgsConstructor
public class IncomeDetailService {

    private final IncomeDetailMapper incomeDetailMapper;
    private final IncomeDetailRepo incomeDetailRepo;
    private final IncomeRepo incomeRepo;

    /**
     * ADD SINGLE INCOME DETAIL TO SPECIFIED INCOME
     */
    public ResponseEntity<ApiResponse> addIncomeDetail(IncomeDetailCreateDto dto) {
        IncomeDetail incomeDetail = incomeDetailMapper.fromCreateDto(dto);
        incomeDetail = incomeDetailRepo.save(incomeDetail);
        IncomeDetailProjection projection = incomeDetailRepo.getIncomeDetailProjectionById(incomeDetail.getId());
        return response(HttpStatus.OK, projection);
    }

    /**
     * GET INCOME DETAILS PAGEABLE
     */
    public ResponseEntity<ApiResponse> getIncomeDetails(Integer page, Integer size) throws PageSizeException {
        Pageable pageable = CommandUtils.simplePageable(page, size, Sort.Direction.DESC, "createdAt");
        List<IncomeDetailProjection> projection = incomeDetailRepo.getIncomeDetailsProjection(pageable);
        return response(HttpStatus.OK, projection);
    }

    /**
     * GET INCOME DETAIL BY ID
     */
    public ResponseEntity<ApiResponse> getIncomeDetailById(UUID id) {
        Objects.requireNonNull(id);
        IncomeDetailProjection projection = incomeDetailRepo
                .getIncomeDetailProjectionByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "INCOME DETAIL"));
        return response(HttpStatus.OK, projection);
    }

    /**
     * GET INCOME DETAILS BY INCOME ID
     */
    public ResponseEntity<ApiResponse> getIncomeDetailsByIncomeId(UUID incomeId) {
        Objects.requireNonNull(incomeId);
        Income income = incomeRepo
                .findById(incomeId)
                .orElseThrow(() -> new ResourceNotFoundException(incomeId, "INCOME"));
        return response(HttpStatus.OK, incomeDetailRepo.getIncomeDetailsProjectionByIncomeId(income.getId()));
    }

    /**
     * DELETE INCOME DETAIL BY ID
     */
    public ResponseEntity<ApiResponse> deleteIncomeDetail(UUID id) {
        if (id == null) throw new NullPointerException("ID OF INCOME DETAIL MUST NOT BE NULL");
        if (!incomeDetailRepo.existsById(id)) throw new ResourceNotFoundException(id, "INCOME DETAIL");
        incomeDetailRepo.deleteById(id);
        return response(HttpStatus.NO_CONTENT, null);
    }

    /**
     * UPDATE INCOME DETAIL FROM DTO
     */
    public ResponseEntity<ApiResponse> updateIncomeDetail(UUID id, IncomeDetailCreateDto source) {
        if (id == null) throw new NullPointerException("INCOME DETAIL ID MUST NOT BE NULL");

        IncomeDetail target = incomeDetailRepo
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "INCOME DETAIL"));

        incomeDetailMapper.fromUpdateDto(source, target);
        incomeDetailRepo.save(target);
        return response(HttpStatus.OK, incomeDetailRepo.getIncomeDetailProjectionById(target.getId()));
    }
}
