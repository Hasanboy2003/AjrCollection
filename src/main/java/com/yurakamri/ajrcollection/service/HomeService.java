package com.yurakamri.ajrcollection.service;

import com.yurakamri.ajrcollection.entity.Advertising;
import com.yurakamri.ajrcollection.entity.Brand;
import com.yurakamri.ajrcollection.entity.Product;
import com.yurakamri.ajrcollection.entity.User;
import com.yurakamri.ajrcollection.payload.AdminElementDto;
import com.yurakamri.ajrcollection.payload.AdvertisingDto;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.home.BrandDtoForHome;
import com.yurakamri.ajrcollection.payload.home.BrandProductDto;
import com.yurakamri.ajrcollection.payload.home.HomeDto;
import com.yurakamri.ajrcollection.payload.product.AllProductDtoForUser;
import com.yurakamri.ajrcollection.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final ProductService productService;
    private final UserRepo userRepo;
    private final OrderRepo orderRepo;
    private final IncomeRepo incomeRepo;
    private final ProductRepository productRepository;

    private final BrandRepository brandRepository;

    private final AdvertisingRepository advertisingRepository;
    public ApiResponse homePage(User user){
        List<Brand> all = brandRepository.findAll();
        List<BrandProductDto> brandProductDtoSet=new ArrayList<>();
        List<BrandDtoForHome> brandDtoForHomeList=new ArrayList<>();
        for (Brand brand : all) {
            if (brand.isActive()){
            ApiResponse allByCategoryIdOrBrandId = productService.getAllByCategoryIdOrBrandId(null, brand.getId(), user, 0, 10);
            List<AllProductDtoForUser> productList= (List<AllProductDtoForUser>) allByCategoryIdOrBrandId.getObject();
            brandProductDtoSet.add(new BrandProductDto(brand.getId(), brand.getName(), productList));
            brandDtoForHomeList.add(new BrandDtoForHome(brand.getId(), brand.getName()));
          }
        }
        List<AdvertisingDto> advertisingDtoList=new ArrayList<>();
        List<Advertising> repositoryAll = advertisingRepository.findAll();
        for (Advertising advertising : repositoryAll) {
            if (advertising.isActive()){
                advertisingDtoList.add(new AdvertisingDto(advertising.getId(),
                        advertising.getAttachment()!=null?advertising.getAttachment().getId():null,
                        advertising.getTitle(), advertising.getDescription(), advertising.getDestination(),
                        advertising.isActive()));
            }
        }

        return new ApiResponse(true, "Home page for user", new HomeDto(
                advertisingDtoList, brandDtoForHomeList, brandProductDtoSet));

    }


    public ApiResponse getAdminElements() {
        long products = productRepository.countProducts();
        long incomes = incomeRepo.countIncomes();
        long orders = orderRepo.countOrders();
        long users = userRepo.countUsers();
        long drivers = userRepo.countDrivers();
        AdminElementDto adminElementDto = new AdminElementDto(orders,users,drivers,products,incomes);
        return new ApiResponse(true,"Admin Elements",adminElementDto);
    }

}
