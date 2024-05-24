package com.yurakamri.ajrcollection.controller;

import com.yurakamri.ajrcollection.exception.PageSizeException;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.req.incomeDetail.IncomeDetailCreateDto;
import com.yurakamri.ajrcollection.service.IncomeDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(BaseUrl.BASE_URL_WEB + "/incomeDetails")
@RequiredArgsConstructor
public class IncomeDetailController {

    private final IncomeDetailService incomeDetailService;

    /**
     * GET INCOME DETAILS PAGEABLE
     */
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse> getIncomeDetails(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                                        @RequestParam(name = "size", required = false, defaultValue = "20") Integer size) throws PageSizeException {
        return incomeDetailService.getIncomeDetails(page, size);
    }

    /**
     * GET INCOME DETAIL BY ID
     */
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getIncomeDetail(@PathVariable UUID id) {
        return incomeDetailService.getIncomeDetailById(id);
    }

    /**
     * GET INCOME DETAILS BY INCOME
     */
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/byIncome/{incomeId}")
    public ResponseEntity<ApiResponse> getIncomeDetailsByIncome(@PathVariable UUID incomeId) {
        return incomeDetailService.getIncomeDetailsByIncomeId(incomeId);
    }

    /**
     * ADD SINGLE INCOME DETAIL
     */
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse> addIncomeDetail(@Valid @RequestBody IncomeDetailCreateDto src) {
        return incomeDetailService.addIncomeDetail(src);
    }

    /**
     * UPDATE INCOME DETAIL BY ID
     */
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateIncomeDetail(@PathVariable UUID id, @Valid @RequestBody IncomeDetailCreateDto src) {
        return incomeDetailService.updateIncomeDetail(id, src);
    }

    /**
     * DELETE INCOME DETAIL BY ID
     */
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteIncomeDetail(@PathVariable UUID id) {
        return incomeDetailService.deleteIncomeDetail(id);
    }
}
