package com.yurakamri.ajrcollection.service;

import com.yurakamri.ajrcollection.entity.*;
import com.yurakamri.ajrcollection.exception.PageSizeException;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.SizeDto;
import com.yurakamri.ajrcollection.payload.product.*;
import com.yurakamri.ajrcollection.projection.ViewProductSearch;
import com.yurakamri.ajrcollection.repository.*;
import com.yurakamri.ajrcollection.utills.AppConstants;
import com.yurakamri.ajrcollection.utills.CommandUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static com.yurakamri.ajrcollection.payload.ApiResponse.response;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final AttachmentRepository attachmentRepository;

    private final ColorRepo colorRepository;

    private final CategoryRepository categoryRepository;

    private final BrandRepository brandRepository;

    private final ProductColorRepository productColorRepository;

    private final ProductAttachmentRepository productAttachmentRepository;

    private final CartRepo cartRepo;

    private final IncomeDetailRepo incomeDetailRepo;

    private final UserSearchRepo userSearchRepo;

    private final UserRepo userRepo;


    public ApiResponse getByIdProduct(UUID id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            return new ApiResponse(false, "Product does not exist");
        }

        Product product = optionalProduct.get();

        ProductDtoGetByIdForAdmin productDtoGetByIdForAdmin = new ProductDtoGetByIdForAdmin(
                product.getId(), product.getName(), product.getDescription(), product.getBrand().getId(),
                product.getCategory().getId(), product.isActive());
        productDtoGetByIdForAdmin.setColorAttachmentDtoList(generateColorAttachments(product));
        productDtoGetByIdForAdmin.setProductColorSizeAmountList(generateAmount(product));

        return new ApiResponse(true, "Product by Id", productDtoGetByIdForAdmin);
    }

    private Set<ProductColorSizeAmount> generateAmount(Product product) {
        Set<ProductColorSizeAmount> productColorSizeAmountList = new HashSet<>();
        List<ProductColor> allByProductId = productColorRepository.findAllByProductId(product.getId());
        Set<Long> productSize = new HashSet<>();
        if (allByProductId != null) {
            for (ProductColor productColor : allByProductId) {
                List<IncomeDetail> allByProductColorId = incomeDetailRepo.findAllByProductColorId(productColor.getId());
                if (allByProductColorId != null) {
                    for (IncomeDetail incomeDetail : allByProductColorId) {
                        productSize.add(incomeDetail.getSize().getId());
                    }
                }
            }
        }

        if (allByProductId != null || productSize != null) {
            for (ProductColor productColor : allByProductId) {
                for (Long aLong : productSize) {
                    Integer quantityByProductIdAndColorIdAndSizeId = cartRepo.findQuantityByProductIdAndColorIdAndSizeId(product.getId(),
                            productColor.getColor().getId(), aLong);
                    productColorSizeAmountList.add(new ProductColorSizeAmount(productColor.getColor().getId(),
                            aLong, quantityByProductIdAndColorIdAndSizeId));
                }
            }
        }
        return productColorSizeAmountList;
    }

    public Set<ColorAttachmentDto> generateColorAttachments(Product product) {
        Set<ColorAttachmentDto> colorAttachmentDtoList = new HashSet<>();

        List<ProductColor> allByProductId = productColorRepository.findAllByProductId(product.getId());
        for (ProductColor productColor : allByProductId){
            List<ProductAttachment> allByProductColorId = productAttachmentRepository.findAllByProductColorId(productColor.getId());
            Set<UUID> attachmentIdList = new HashSet<>();
            for (ProductAttachment productAttachment : allByProductColorId) {
                attachmentIdList.add(productAttachment.getAttachment().getId());
            }

            colorAttachmentDtoList.add(new ColorAttachmentDto(productColor.getColor().getId(), productColor.getColor().getCode(),
                    attachmentIdList));
            System.out.println(allByProductId);

        }
        System.out.println(colorAttachmentDtoList);
        return colorAttachmentDtoList;
    }


    public ApiResponse addProduct(ProductDto productDto) {
        if (productRepository.existsAllByName(productDto.getName())) {
            return new ApiResponse(false, "Siz kiritgan nomdagi mahsulot mavjud");
        }
        Optional<Category> optionalCategory = categoryRepository.findById(productDto.getCategoryId());
        if (optionalCategory.isEmpty()) {
            return new ApiResponse(false, "Siz kategory tanlamagansiz");
        }

        Optional<Brand> optionalBrand = brandRepository.findById(productDto.getBrandId());
        if (optionalBrand.isEmpty()) {
            return new ApiResponse(false, "Siz brand tanlamagansiz");
        }


        Product product = new Product(productDto.getName(), optionalBrand.get(), optionalCategory.get(),
                productDto.isActive(), productDto.getDescription(), productDto.getOutcomePrice(),
                generateProductCode(), null);
        Product savedProduct = productRepository.save(product);
        if (productDto.getColorAttachmentList().size() != 0) {
            for (ColorAttachment colorAttachment : productDto.getColorAttachmentList()) {
                ApiResponse apiResponse = saveColorAttachment(savedProduct, colorAttachment);
                if (!apiResponse.isSuccess()) {
                    return apiResponse;
                }

            }

        }
        return new ApiResponse(true, "Mahsulot saqlandi");
    }


    public ApiResponse saveColorAttachment(Product product, ColorAttachment colorAttachment) {
        if (!colorRepository.existsById(colorAttachment.getColorId())) {
            return new ApiResponse(false, "Siz kiritgan rang mavjud emas");
        }
        if (colorAttachment.getAttachmentIdList().size() == 0) {
            return new ApiResponse(false, "Siz kiritgan rasmlar mavjud emas");
        }

        for (UUID uuid : colorAttachment.getAttachmentIdList()) {
            boolean existsById = attachmentRepository.existsById(uuid);
            if (!existsById) {
                return new ApiResponse(false, "Siz kiritgan rasm mavjud emas");
            }
        }

        Color color = colorRepository.getById(colorAttachment.getColorId());
        ProductColor savedProductColor = productColorRepository.save(new ProductColor(product, color));
        for (UUID uuid : colorAttachment.getAttachmentIdList()) {
            productAttachmentRepository.save(new ProductAttachment(savedProductColor, attachmentRepository.getById(uuid)));
        }

        return new ApiResponse(true, "Saqlandi");

    }

    public ApiResponse editProduct(UUID id, ProductDto productDto) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            return new ApiResponse(false, "Siz so`ragan Mahsulod mavjud emas");
        }
        Optional<Category> optionalCategory = categoryRepository.findById(productDto.getCategoryId());
        if (optionalCategory.isEmpty()) {
            return new ApiResponse(false, "Siz kategory tanlamagansiz");
        }

        Optional<Brand> optionalBrand = brandRepository.findById(productDto.getBrandId());
        if (optionalBrand.isEmpty()) {
            return new ApiResponse(false, "Siz brand tanlamagansiz");
        }

        Product product = optionalProduct.get();
        product.setBrand(optionalBrand.get());
        product.setOutcomePrice(productDto.getOutcomePrice());
        product.setActive(productDto.isActive());
        product.setDescription(productDto.getDescription());
        product.setName(productDto.getName());
        product.setCategory(optionalCategory.get());
        try {
            Product savedProduct = productRepository.save(product);
        } catch (Exception e) {
            return new ApiResponse(false, "Siz kiritgan nomli Mahsulot mavjud");
        }

        if (productDto.getColorAttachmentList().size() != 0) {
            for (ColorAttachment colorAttachment : productDto.getColorAttachmentList()) {
                ApiResponse apiResponse = saveColorAttachment(product, colorAttachment);
                if (!apiResponse.isSuccess()) {
                    return apiResponse;
                }

            }

        }
        return new ApiResponse(true, "Mahsulot tahrirlandi");
    }

    //    @Transactional
    public ApiResponse deleteProduct(UUID id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            boolean exist = false;

            try {
                List<ProductColor> allByProductId = productColorRepository.findAllByProductId(id);
                for (ProductColor productColor : allByProductId) {
                    List<IncomeDetail> allByProductColorId = incomeDetailRepo.findAllByProductColorId(productColor.getId());
                    if (allByProductColorId.size() == 0) {
                        exist = true;
                    }
                }

                if (!exist) {
                    Set<UUID> deleteAttachment = new HashSet<>();
                    Set<UUID> deleteProductAttachment = new HashSet<>();
                    Set<UUID> deleteProductColor = new HashSet<>();

                    for (ProductColor productColor : allByProductId) {
                        List<ProductAttachment> allByProductColorId = productAttachmentRepository.findAllByProductColorId(id);
                        for (ProductAttachment productAttachment : allByProductColorId) {
                            deleteAttachment.add(productAttachment.getAttachment().getId());
                            deleteProductAttachment.add(productAttachment.getId());
                        }
                        deleteProductColor.add(productColor.getId());
                    }

                    for (UUID uuid : deleteProductAttachment) {
                        productAttachmentRepository.deleteById(uuid);
                    }

                    for (UUID uuid : deleteAttachment) {
                        attachmentRepository.deleteById(uuid);
                    }

                    for (UUID uuid : deleteProductColor) {
                        productColorRepository.deleteById(uuid);
                    }
                    productRepository.deleteById(id);
                    return new ApiResponse(true, "Mahsulot o`chirildi");
                }
            } catch (Exception e) {
                return new ApiResponse(false, "Mahsulotni o`chirish mumkin emas, " +
                        "uni aktivligini o`chirib qo`yishingiz mumiin");
            }
        }
        return new ApiResponse(false, "Siz so`ragan product mavjud emas");
    }

    public ApiResponse enabledOrDisabled(UUID id, boolean isActive) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            optionalProduct.get().setActive(isActive);
            productRepository.save(optionalProduct.get());
            return new ApiResponse(true, isActive ? "Mahsulot aktivlashtirishdi" :
                    "Mahsulot aktivligi o`chirildi");
        }
        return new ApiResponse(false, "Siz tanalagan Mahsulot mavjud emas");
    }

    public String generateProductCode() {
        String code;
        Optional<String> codeOptional = productRepository.byCodeFromNativeQuery();
        if (codeOptional.isPresent()) {
            code = codeOptional.get();
            while (true) {
                long codeLong = Long.parseLong(code);
                codeLong++;
                code = String.valueOf(codeLong);
                if (!productRepository.existsAllByCode(code)) {
                    return code;
                }
            }
        }
        return "1";
    }

    public ApiResponse outcomePriceEdit(UUID id, BigDecimal newPrice) {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isEmpty()) return new ApiResponse(false, "Siz kiritgan Id lik mavsulot mavjud emas");
        Product product = byId.get();
        product.setOutcomePrice(newPrice);
        Product editedProduct = productRepository.save(product);
        return new ApiResponse(true, "Yangi narx qabul qilindi");
    }


    public ProductDTOForAdmin generateProductDtoForAdmin(Product product) {
        UUID attachmentId = null;
        Set<ColorAttachmentDto> colorAttachmentDtoList = generateColorAttachments(product);
        for (ColorAttachmentDto colorAttachmentDto : colorAttachmentDtoList) {
            for (UUID uuid : colorAttachmentDto.getAttachmentIdList()) {
                if (uuid != null)
                    attachmentId = uuid;
            }
        }

        Set<ProductColorSizeAmount> productColorSizeAmounts = generateAmount(product);
        int amount=0;

        for (ProductColorSizeAmount productColorSizeAmount : productColorSizeAmounts) {
            amount+=productColorSizeAmount.getAmount();
        }


        ProductDTOForAdmin productDTOForAdmin = new ProductDTOForAdmin(product.getId(), product.getName(), product.getBrand().getId(),
                product.getOutcomePrice(),
                amount, product.getCreatedAt(), product.isActive(), attachmentId);
        System.out.println(productDTOForAdmin);
        return productDTOForAdmin;
    }

    public ApiResponse getAllProductByAdmin(boolean all) {
        List<Product> allProduct = productRepository.findAll();
        List<ProductDTOForAdmin> productDTOForAdmins = new ArrayList<>();
        if (all) {
            for (Product product : allProduct) {
                productDTOForAdmins.add(generateProductDtoForAdmin(product));
            }
            return new ApiResponse(true, "ALL PRODUCTS", productDTOForAdmins);
        }

        List<ProductDtoForSales> productDtoForSalesList = new ArrayList<>();
        for (Product product : allProduct) {
            productDtoForSalesList.add(new ProductDtoForSales(product.getId(), product.getName()));
        }

        return new ApiResponse(true, "all products for sales", productDtoForSalesList);
    }


    public ApiResponse getProductColors(UUID id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            return new ApiResponse(false, "Product mavjud emas");
        }
        List<ProductColor> allByProductId = productColorRepository.findAllByProductId(id);
        Set<ColorDtoForIncome> colorDtoForIncomeHashSet = new HashSet<>();
        for (ProductColor productColor : allByProductId) {
            colorDtoForIncomeHashSet.add(new ColorDtoForIncome(productColor.getColor().getId(),
                    productColor.getColor().getCode()));
        }

        ProductColorDto productColorDto = new ProductColorDto(id, optionalProduct.get().getName(),
                optionalProduct.get().getCode(), colorDtoForIncomeHashSet);
        return new ApiResponse(true, "Productning colorlari", productColorDto);
    }


    public ApiResponse getAllProduct(User user, int page, int size) {
        try {
            Pageable pageable = CommandUtils.descOrAscByCreatedAtPageable(page, size, true);
            Page<Product> productRepositoryAll = productRepository.findAllByOrderByCreatedAtDesc(pageable);

            for (Product product : productRepositoryAll) {
                product.toString();
            }
            List<AllProductDtoForUser> productDtoForUserList = new ArrayList<>();
            for (Product product : productRepositoryAll) {
                if (product.isActive() && product.getBrand().isActive() && product.getCategory().isActive()) {
                    productDtoForUserList.add(generateAllProductDtoForUser(user, product));
                }
            }


            return new ApiResponse(true, "Products", productDtoForUserList);

        } catch (PageSizeException e) {
            throw new RuntimeException(e);
        }
    }

    private AllProductDtoForUser generateAllProductDtoForUser(User user, Product product) {
        UUID attachmentId = null;
        boolean isFavourites = false;

        Set<ColorAttachmentDto> colorAttachmentDtoList = generateColorAttachments(product);
        for (ColorAttachmentDto colorAttachmentDto : colorAttachmentDtoList) {
            for (UUID uuid : colorAttachmentDto.getAttachmentIdList()) {
                if (uuid != null)
                    attachmentId = uuid;
            }
        }

        if (user != null && !user.getFavourites().isEmpty()) {
            for (Product favourite : user.getFavourites()) {
                if (product.getId().equals(favourite.getId())) {
                    isFavourites = true;
                    break;
                }
            }
        }
        AllProductDtoForUser allProductDtoForUser = new AllProductDtoForUser(
                product.getId(), product.getName(), attachmentId, product.getOutcomePrice(),
                new BigDecimal(0), product.getOutcomePrice(), product.getBrand().getName(),
               isFavourites
        );

        if (product.getDiscount() != null) {
            long daysBetween = DAYS.between(product.getDiscount().getEndDate(), LocalDate.now());
            if (daysBetween >= 0) {
                allProductDtoForUser.setDiscount(product.getDiscount().getPercent());
                allProductDtoForUser.setSellingPrice(sumDiscount(product));
            }
        }

        return allProductDtoForUser;
    }

    private BigDecimal sumDiscount(Product product) {
        BigDecimal productSales = product.getDiscount().getPercent().divide(new BigDecimal(100));
        BigDecimal productPriceSales = product.getOutcomePrice().multiply(productSales);
        return product.getOutcomePrice().subtract(productPriceSales);
    }


    public ApiResponse getByIdProductForUser(User user, UUID id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            return new ApiResponse(false, "Mahsulot mavjud emas");
        }

        if (!optionalProduct.get().isActive() && optionalProduct.get().getBrand().isActive()
                && optionalProduct.get().getCategory().isActive()) {
            return new ApiResponse(false, "Mahsulot bloklangan");
        }

        Product product = optionalProduct.get();
        List<ProductColor> allByProductColor = productColorRepository.findAllByProductId(product.getId());
        Set<SizeDto> sizList = new HashSet<>();
        if (!allByProductColor.isEmpty()) {
            for (ProductColor productColor : allByProductColor) {
                List<IncomeDetail> allByProductColorId = incomeDetailRepo.findAllByProductColorId(productColor.getId());
                if (!allByProductColorId.isEmpty()) {
                    for (IncomeDetail incomeDetail : allByProductColorId) {
                        sizList.add(new SizeDto(incomeDetail.getSize().getId(), incomeDetail.getSize().getName(), incomeDetail.getSize().isActive()));
                    }
                }
            }
        }

        Set<ColorAttachmentDto> colorAttachmentDtos = generateColorAttachments(product);
        boolean isFavourites = false;
        if (user != null && !user.getFavourites().isEmpty()) {
            for (Product favourite : user.getFavourites()) {
                if (product.getId().equals(favourite.getId())) {
                    isFavourites = true;
                    break;
                }
            }
        }



        ProductDtoByIdForUser productDtoByIdForUser= new ProductDtoByIdForUser(
                product.getId(), product.getName(), product.getDescription(), product.getBrand().getName(),
                product.getOutcomePrice(), new BigDecimal(0),
                product.getOutcomePrice(), sizList.isEmpty() ? null : sizList,
                colorAttachmentDtos, isFavourites);

        if (product.getDiscount() != null) {
            long daysBetween = DAYS.between(product.getDiscount().getEndDate(), LocalDate.now());
            if (daysBetween >= 0) {
                productDtoByIdForUser.setDiscount(product.getDiscount().getPercent());
                productDtoByIdForUser.setSellingPrice(sumDiscount(product));
            }
        }
        return new ApiResponse(true, "Product By Id for User", productDtoByIdForUser);
    }


    @SneakyThrows
    public ApiResponse getAllByCategoryIdOrBrandId(UUID categoryId, Long brandId, User user,
                                                   int page, int size) {
        List<Product> productList = new ArrayList<>();
        Pageable pageable = CommandUtils.descOrAscByCreatedAtPageable(page, size, true);
        if (categoryId != null && brandId != null) {
            if (categoryRepository.existsById(categoryId) && brandRepository.existsById(brandId)) {
                Page<Product> allProduct = productRepository.findAllByBrandIdAndCategoryIdOrCategory_ParentCategoryIdOrderByCreatedAtDesc(
                        brandId, categoryId, categoryId, pageable);
                for (Product product : allProduct) {
                    productList.add(product);
                }
            }
        }

        if (brandId != null && categoryId == null) {
            Page<Product> allProduct = productRepository.findAllByBrandIdOrderByCreatedAtDesc(brandId, pageable);
            for (Product product : allProduct) {
                productList.add(product);
            }
        }

        if (brandId == null && categoryId != null) {
            Page<Product> allProduct = productRepository.findAllByCategoryIdOrCategory_ParentCategoryIdOrderByCreatedAtDesc(
                    categoryId, categoryId, pageable);
            for (Product product : allProduct) {
                productList.add(product);
            }
        }
        List<AllProductDtoForUser> productDtoForUserList = new ArrayList<>();
        for (Product product : productList) {
            if (product.isActive() && product.getBrand().isActive() && product.getCategory().isActive()) {
                productDtoForUserList.add(generateAllProductDtoForUser(user, product));
            }
        }

        return new ApiResponse(true, "Products", productDtoForUserList);
    }

    /**
     * SEARCH HISTORY
     */
    public ResponseEntity<ApiResponse> getSearchHistory(User user) {
        return response(HttpStatus.OK, userSearchRepo.findAllByUserIdOrderByCreatedAtDesc(user.getId()));
    }

    /**
     * SEARCH
     */
    public ResponseEntity<ApiResponse> search(String searchText, Integer page, Integer size, UserDetails userDetails) throws PageSizeException {
        CommandUtils.validatePageAndSize(page, size);
        PageRequest pageable = PageRequest.of(page, size);

        if (Objects.isNull(userDetails)) {
            List<ViewProductSearch> products = productRepository.searchProducts(searchText, null, pageable);
            return response(products.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK, products);
        }

        User currentUser = userRepo
                .findByPhoneNumber(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("USER WITH PHONE NUMBER : " + userDetails.getUsername() + " NOT FOUND!"));

        List<ViewProductSearch> products = productRepository.searchProducts(searchText, currentUser.getId(), pageable);
        if (!products.isEmpty()) {
            if (!userSearchRepo.existsByUserIdAndSearchText(currentUser.getId(), searchText)) {
                userSearchRepo.save(new UserSearch(searchText, currentUser));
                if (currentUser.getUserSearches().size() > AppConstants.MAX_USER_SEARCH) {
                    UserSearch lastSearch = userSearchRepo.getLastUserSearch(currentUser.getId());
                    userSearchRepo.delete(lastSearch);
                }
            }
            return response(HttpStatus.OK, products);
        }
        return response(HttpStatus.NO_CONTENT, List.of());
    }
}
