package com.yurakamri.ajrcollection.controller;

import com.yurakamri.ajrcollection.exception.PageSizeException;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.req.income.IncomeCreateDto;
import com.yurakamri.ajrcollection.payload.req.income.IncomeUpdateDto;
import com.yurakamri.ajrcollection.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(BaseUrl.BASE_URL_WEB + "/incomes")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    /**
     * FETCH INCOMES
     */
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse> getIncomes(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                                  @RequestParam(name = "size", required = false, defaultValue = "20") Integer size) throws PageSizeException {
        return incomeService.getIncomes(page, size);
    }

    /**
     * FETCH SINGLE INCOME
     */
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/{incomeId}")
    public ResponseEntity<ApiResponse> getIncome(@PathVariable UUID incomeId) {
        return incomeService.getIncome(incomeId);
    }

    /**
     * ADD NEW INCOME
     */
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse> addIncome(@Valid @RequestBody IncomeCreateDto dto) {
        return incomeService.addIncome(dto);
    }

    /**
     * UPDATE INCOME BY ID
     */
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PutMapping("/{incomeId}")
    public ResponseEntity<ApiResponse> updateById(@PathVariable UUID incomeId, @Valid @RequestBody IncomeUpdateDto dto) {
        return incomeService.updateIncome(incomeId, dto);
    }
}
