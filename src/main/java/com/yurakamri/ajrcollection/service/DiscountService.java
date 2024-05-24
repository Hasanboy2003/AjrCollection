package com.yurakamri.ajrcollection.service;

import com.yurakamri.ajrcollection.entity.Discount;
import com.yurakamri.ajrcollection.entity.Product;
import com.yurakamri.ajrcollection.exception.ResourceNotFoundException;
import com.yurakamri.ajrcollection.mapper.DiscountMapper;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.ProductDiscountDto;
import com.yurakamri.ajrcollection.payload.product.dicount.DiscountCreateDto;
import com.yurakamri.ajrcollection.payload.product.dicount.DiscountUpdateDto;
import com.yurakamri.ajrcollection.repository.DiscountRepo;
import com.yurakamri.ajrcollection.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.yurakamri.ajrcollection.payload.ApiResponse.response;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Transactional
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepo discountRepo;
    private final DiscountMapper discountMapper;
    private final ProductRepository productRepo;

    /**
     * GET DISCOUNTS
     */
    public ResponseEntity<ApiResponse> getDiscounts() {
        List<ProductDiscountDto> productDiscountDtoList = new LinkedList<>();
        List<Discount> discounts = discountRepo.findAll();
        for (Discount discount : discounts) {
            List<ProductDiscountDto> discountDtos = discount.getProducts().stream().map(product -> generateDiscountDto(discount, product)).collect(Collectors.toList());
            productDiscountDtoList.addAll(discountDtos);
        }
        return response(HttpStatus.OK, productDiscountDtoList);
    }

    /**
     * GET SINGLE DISCOUNT BY ID
     */
    public ResponseEntity<ApiResponse> getDiscount(Long id) {
        if (id == null) throw new NullPointerException("DISCOUNT ID MUST NOT BE NULL");
        if (!discountRepo.existsById(id)) throw new ResourceNotFoundException(id, "DISCOUNT");
        Discount discount = discountRepo.getById(id);
        Stream<ProductDiscountDto> productDiscountDto = discount.getProducts().stream().map(product -> generateDiscountDto(discount, product));
        return response(HttpStatus.OK, productDiscountDto);
    }

    /**
     * CREATE DISCOUNT WITH PRODUCTS OR ITSELF
     */
    public ResponseEntity<ApiResponse> createDiscount(DiscountCreateDto dto) {
        if (!dto.getEndDate().isAfter(dto.getStartDate()) || !LocalDate.now().isBefore(dto.getEndDate())) {
            return response(false, "START_DATE < END_DATE && NOW < END_DATE");
        }
        Discount discount = discountMapper.fromCreateDto(dto);

        if (discount.getProducts() != null) {
            for (Product product : discount.getProducts()) {
                product.setDiscount(discount);
                productRepo.save(product);
            }
        }
        discountRepo.save(discount);
        return response(HttpStatus.CREATED, discountRepo.getDiscountProjectionById(discount.getId()));
    }

    /**
     * DELETE DISCOUNT
     */
    public ResponseEntity<ApiResponse> delete(Long id, UUID productId) {
        Discount discount = discountRepo
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "DISCOUNT"));
        Product product = productRepo
                .findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(productId, "PRODUCT"));
        List<Product> products = discount.getProducts();
        products.removeIf(product1 -> product1.getName().equals(product.getName()));
        product.setDiscount(null);
        productRepo.save(product);
        discount.setProducts(products);
        discountRepo.save(discount);
        return response(true, null);
    }

    /**
     * UPDATE DISCOUNT
     */
    public ResponseEntity<ApiResponse> editDiscount(Long id, DiscountUpdateDto src, UUID productId) {
        if (!src.getEndDate().isAfter(src.getStartDate()) || !LocalDate.now().isBefore(src.getEndDate())) {
            return response(false, "START_DATE < END_DATE && NOW < END_DATE");
        }
        Discount target = discountRepo
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "DISCOUNT"));
        Product product = productRepo
                .findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(productId, "PRODUCT"));
        Discount discount = new Discount(src.getStartDate(), src.getEndDate(), src.getPercent(), List.of(product));
        target.getProducts().removeIf(item -> item.getName().equals(product.getName()));
        discountRepo.save(target);
        discountRepo.saveAndFlush(discount);
        product.setDiscount(discount);
        productRepo.save(product);
        return response(true, "PRODUCT DISCOUNT UPDATED");
    }


    /**
     * PUT SPECIFIC PRODUCT ON DISCOUNT
     */
    public ResponseEntity<ApiResponse> putOnDiscount(Long discountId, UUID productId) {
        Discount discount = discountRepo
                .findById(discountId)
                .orElseThrow(() -> new ResourceNotFoundException(discountId, "DISCOUNT"));

        Product product = productRepo
                .findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(productId, "PRODUCT"));

        product.setDiscount(discount);
        productRepo.save(product);
        return response(HttpStatus.OK, discountRepo.getDiscountProjectionById(discount.getId()));
    }

    /**
     * DELETES DISCOUNT FROM SPECIFIC PRODUCT
     */
    public ResponseEntity<ApiResponse> takeFromDiscount(UUID productId) {
        Product product = productRepo
                .findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(productId, "PRODUCT"));

        Discount discount = product.getDiscount();
        if (discount != null) {
            product.setDiscount(null);
            productRepo.save(product);
            return response(HttpStatus.OK, discountRepo.getDiscountProjectionById(discount.getId()));
        }
        return response(false, "PRODUCT IS NOT ON DISCOUNT");
    }

    public ProductDiscountDto generateDiscountDto(Discount discount, Product product) {
        boolean valid = discount != null && DAYS.between(LocalDate.now(), discount.getEndDate()) >= 0;
        double value = product.getOutcomePrice().doubleValue();
        assert discount != null;
        return new ProductDiscountDto(discount.getId(), product.getId(),
                discount.getStartDate(),
                discount.getEndDate(),
                discount.getPercent(),
                product.getOutcomePrice(),
                valid ? new BigDecimal(value - (value * discount.getPercent().doubleValue() / 100)) : product.getOutcomePrice(),
                product.getName(), product.getBrand().getName(),
                DAYS.between(LocalDate.now(), discount.getEndDate()) >= 0
        );
    }
}
