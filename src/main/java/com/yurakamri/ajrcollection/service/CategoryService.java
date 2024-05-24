package com.yurakamri.ajrcollection.service;

import com.yurakamri.ajrcollection.entity.Category;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.CategoryDto;
import com.yurakamri.ajrcollection.payload.CategoryDtoForUser;
import com.yurakamri.ajrcollection.repository.BrandRepository;
import com.yurakamri.ajrcollection.repository.CategoryRepository;
import com.yurakamri.ajrcollection.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final BrandRepository brandRepository;

    private final ProductRepository productRepository;

    public ApiResponse editCategory(UUID id, CategoryDto categoryDto) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()){
             try {
                 Category category=optionalCategory.get();
                 if (categoryDto.getParentCategoryId()!=null) {
                     category.setParentCategory(categoryRepository.findById(categoryDto.getParentCategoryId()).orElse(null));
                 }category.setActive(category.isActive());
                 category.setName(categoryDto.getName());
                 category.setDescription(categoryDto.getDescription());
                  categoryRepository.save(category);
                 return new ApiResponse(true, "Category tahrirlandi");
             }catch (Exception e){
                 return new ApiResponse(false, "Siz kiritgan "
                         +categoryDto.getName()+" lik Category mavjud");
             }
        }return new ApiResponse(false, "Siz kiritgan Category topilmadi");

    }

    public ApiResponse getAllCategory() {
        List<Category> categoryList = categoryRepository.findAll();
        if (categoryList.size()==0){
            return new ApiResponse(false, "Category mavjud emas");
        }

        return new ApiResponse(true, "ALL Category", categoryList.stream().map(this::generateCategoryDto));
    }

    public ApiResponse getByIdCategory(UUID id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        return optionalCategory.map(category -> new ApiResponse(true, "Category : ", generateCategoryDto(category))).orElseGet(() -> new ApiResponse(false, "Siz so`ragan category topilmadi"));
    }

    public ApiResponse addCategory(CategoryDto categoryDto) {
        if (categoryRepository.existsAllByName(categoryDto.getName())){
            return new ApiResponse(false, "Siz kiritgan "+categoryDto.getName()+" lik Category mavjud");
        }
        Category category=new Category(categoryDto.getName(), categoryDto.getDescription(), categoryDto.isActive());
        if (categoryDto.getParentCategoryId()!=null){
            category.setParentCategory(categoryRepository.findById(categoryDto.getParentCategoryId()).orElse(null));
        }
        categoryRepository.save(category);
        return new ApiResponse(true, "Category saqlandi");
    }

    public ApiResponse deleteCategory(UUID id){
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()){
            try {
                categoryRepository.deleteById(id);
                return new ApiResponse(true, "Category o`chirildi.", optionalCategory.get());
            }catch (Exception e){
                return new ApiResponse(false, "Categoryni o`chirib bo`lmaydi. " +
                        "Categoryni aktivligini o`chirib qo`yishingiz mumkin");
            }
        }return new ApiResponse(false, "Siz kiritgan Category topilmadi");

    }

    public ApiResponse enabledOrDisabled(UUID id, boolean active) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()){
            optionalCategory.get().setActive(active);
            categoryRepository.save(optionalCategory.get());
            return new ApiResponse(true, active?"Category aktivlashtirildi":
                    "Category bloklandi", optionalCategory.get());
        }return new ApiResponse(false, "Siz tanlagan Category topilmadi");
    }

    public ApiResponse getAllCategoryForUser() {
        List<Category> allCategory = categoryRepository.findAll();
        List<CategoryDto> categoryDtos=new ArrayList<>();
        for (Category category : allCategory) {
            if (category.isActive()){
                categoryDtos.add(generateCategoryDto(category));
            }
        }

        return new ApiResponse(true, "Category All", categoryDtos);

    }

    public CategoryDto generateCategoryDto(Category category){
        return new CategoryDto(category.getId(), category.getName(), category.getDescription(),
                category.isActive(), category.getParentCategory()!=null?category.getParentCategory().getId():null);
    }

    public ApiResponse getAllCategoryByBrandId(Long brandId) {

        if (!brandRepository.existsById(brandId)) return new ApiResponse(false, "Brand mavjud emas");
        List<CategoryDtoForUser> categoryDtoForUserList=new ArrayList<>();
        List<Category> allByBrandId = categoryRepository.getAllByBrandId(brandId);
        for (Category category : allByBrandId) {
            if (category.isActive()){
                categoryDtoForUserList.add(new CategoryDtoForUser(category.getId(), category.getName()));
            }
        }

        return new ApiResponse(true, "ALL Category by Brand", categoryDtoForUserList);
    }
}
