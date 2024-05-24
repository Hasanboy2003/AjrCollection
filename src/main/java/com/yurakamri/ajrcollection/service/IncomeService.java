package com.yurakamri.ajrcollection.service;

import com.yurakamri.ajrcollection.entity.Income;
import com.yurakamri.ajrcollection.exception.PageSizeException;
import com.yurakamri.ajrcollection.exception.ResourceNotFoundException;
import com.yurakamri.ajrcollection.mapper.IncomeDetailMapper;
import com.yurakamri.ajrcollection.mapper.IncomeMapper;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.req.income.IncomeCreateDto;
import com.yurakamri.ajrcollection.payload.req.income.IncomeUpdateDto;
import com.yurakamri.ajrcollection.payload.req.incomeDetail.IncomeDetailCreateDto;
import com.yurakamri.ajrcollection.projection.income.IncomeProjection;
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
import java.util.UUID;

import static com.yurakamri.ajrcollection.payload.ApiResponse.response;

@Service
@Transactional
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepo incomeRepo;
    private final IncomeMapper mapper;
    private final IncomeDetailMapper incomeDetailMapper;
    private final IncomeDetailRepo incomeDetailRepo;

    /**
     * GET INCOMES WITH SOME INCOME DETAILS INFORMATION
     */
    public ResponseEntity<ApiResponse> getIncomes(Integer page, Integer size) throws PageSizeException {
        Pageable pageable = CommandUtils.simplePageable(page, size, Sort.Direction.ASC, "incomeCode");
        List<IncomeProjection> response = incomeRepo.getIncomesProjectionPageable(pageable);
        return response(HttpStatus.OK, response);
    }

    /**
     * GET INCOME WITH THE SPECIFIED ID
     */
    public ResponseEntity<ApiResponse> getIncome(UUID incomeId) {
        IncomeProjection projection = incomeRepo
                .getIncomeProjectionOptional(incomeId)
                .orElseThrow(() -> new ResourceNotFoundException(incomeId, "INCOME"));

        return response(HttpStatus.OK, projection);
    }

    /**
     * ADD NEW INCOME FROM DTO OBJECT
     */
    public ResponseEntity<ApiResponse> addIncome(IncomeCreateDto dto) {
        Income income = mapper.fromCreateDto(dto);
        income = incomeRepo.save(income);

        List<IncomeDetailCreateDto> incomeDetailDtoList = dto.getIncomeDetails();
        for (IncomeDetailCreateDto incomeDetailDto : incomeDetailDtoList) {
            incomeDetailDto.setIncomeId(income.getId());
            incomeDetailRepo.save(incomeDetailMapper.fromCreateDto(incomeDetailDto));
        }
        IncomeProjection projection = incomeRepo.getIncomeProjection(income.getId());
        return response(HttpStatus.CREATED, projection);
    }

    /**
     * UPDATE INCOME FROM DTO
     */
    public ResponseEntity<ApiResponse> updateIncome(UUID id, IncomeUpdateDto dto) {
        Income income = incomeRepo
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "INCOME"));

        mapper.fromUpdateDto(dto, income);
        incomeRepo.save(income);
        return response(HttpStatus.OK, incomeRepo.getIncomeProjection(income.getId()));
    }
}
