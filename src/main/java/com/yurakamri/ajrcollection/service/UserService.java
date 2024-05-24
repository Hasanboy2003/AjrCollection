package com.yurakamri.ajrcollection.service;

import com.yurakamri.ajrcollection.entity.*;
import com.yurakamri.ajrcollection.entity.abs.AbsLongEntity;
import com.yurakamri.ajrcollection.entity.enums.RoleEnum;
import com.yurakamri.ajrcollection.exception.PageSizeException;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.SizeDto;
import com.yurakamri.ajrcollection.payload.UserDto;
import com.yurakamri.ajrcollection.payload.product.ColorAttachmentDto;
import com.yurakamri.ajrcollection.payload.product.ProductDtoByIdForUser;
import com.yurakamri.ajrcollection.payload.userForAdmin.UserDTOForAdmin;
import com.yurakamri.ajrcollection.repository.AttachmentRepo;
import com.yurakamri.ajrcollection.repository.ProductRepository;
import com.yurakamri.ajrcollection.repository.SizeRepository;
import com.yurakamri.ajrcollection.repository.UserRepo;
import com.yurakamri.ajrcollection.utills.CommandUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final SizeRepository sizeRepository;

    private final AttachmentRepo attachmentRepo;

    public UserDto generateUserDto(User user) {
        return new UserDto(user.getId(), user.getPhoneNumber(), user.getFirstName(), user.getLastName(), user.getAttachment() != null ? user.getAttachment().getId() : null, user.getLanguage());
    }

    public ApiResponse getAllUserForAdmin() {

        List<UserDTOForAdmin> userDTOForAdminList = userRepo.findAllByRoleRoleName(RoleEnum.ROLE_USER).stream().map(this::generateUserDtoForAdmin).collect(Collectors.toList());
        if(userDTOForAdminList.isEmpty())
         return new ApiResponse(false,"USERS DOES NOT EXIST");

        return new ApiResponse(true, "ALL USERS", userDTOForAdminList);
    }

    public UserDTOForAdmin generateUserDtoForAdmin(User user) {
        return new UserDTOForAdmin(user.getId(), user.getPhoneNumber(), user.getFirstName(), user.getLastName(), user.getAttachment() != null ? user.getAttachment().getId() : null, user.isEnabled(), user.getCreatedAt());
    }

    public ApiResponse deleteUserForAdmin(UUID id, User userActive) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()) {
            if (userActive.getPhoneNumber().equals(optionalUser.get().getPhoneNumber())) {
                return new ApiResponse(false, "Siz o`zingizni o`chira olmaysiz");
            }

            try {
                userRepo.deleteById(id);
                return new ApiResponse(true, "O`chirildi");
            } catch (Exception e) {
                User user = optionalUser.get();
                String phoneNumber = user.getPhoneNumber();
                user.setEnabled(false);
                user.setPhoneNumber(phoneNumber + UUID.randomUUID());
                userRepo.save(user);
                return new ApiResponse(true, "User arxivlandi. " + "Bunday User endi tizimga kira olmaydi.");
            }
        }
        return new ApiResponse(false, "Bunday Id lik user mavjud emas");
    }

    public ApiResponse enableOrDisable(UUID id, boolean active) {
        boolean existsById = userRepo.existsById(id);
        if (!existsById) {
            return new ApiResponse(false, "Siz so`ragan User topilmadi");
        }

        User user = userRepo.getById(id);
        user.setEnabled(active);
        User save = userRepo.save(user);
        return new ApiResponse(true, "User " + (active ? "aktivlashtirildi" : "bloklandi"));
    }

    public ApiResponse deleteUserForUser(UUID id) {
        boolean existsById = userRepo.existsById(id);
        if (!existsById) {
            return new ApiResponse(false, "Bunday user mavjud emas");
        }

        User user = userRepo.getById(id);
        try {
            userRepo.deleteById(id);
            return new ApiResponse(true, "User o`chirildi");
        } catch (Exception e) {
            user.setEnabled(false);
            String phoneNumber = user.getPhoneNumber();
            user.setPhoneNumber(phoneNumber + UUID.randomUUID());
            userRepo.save(user);
            return new ApiResponse(true, "User arxivlandi");
        }
    }

    public ApiResponse addAttachmentByUser(User user, UUID attachmentId) {
        boolean existsById = attachmentRepo.existsById(attachmentId);
        if (!existsById) {
            return new ApiResponse(false, "Attachment topilmadi");
        }
        Attachment attachment = attachmentRepo.getById(attachmentId);
        user.setAttachment(attachment);
        userRepo.save(user);
        return new ApiResponse(true, "Avatar Userga biriktirildi");
    }

    public ApiResponse addFavourites(UUID id, User user) {
        if (!productRepository.existsById(id)) return new ApiResponse(false, "PRODUCT DOES NOT EXIST");
        Product product = productRepository.getById(id);
        boolean anyMatch = user.getFavourites().stream().anyMatch(item -> item.getName().equals(product.getName()));
        List<Product> favourites = user.getFavourites();
        if (anyMatch)
            favourites.removeIf(product1 -> product1.getName().equals(product.getName()));
        else
            favourites.add(product);
        userRepo.save(user);
        return new ApiResponse(true, anyMatch?"REMOVED FROM FAVOURITES":"PRODUCT ADDED TO FAVOURITES");
    }

    public ApiResponse getFavourites(User user) {
        if (user.getFavourites().isEmpty()) return new ApiResponse(false, "USER DOESN'T HAVE FAVOURITE PRODUCT");
        List<ProductDtoByIdForUser> favourites = user.getFavourites().stream().map(this::generateProductDto).collect(Collectors.toList());
        return new ApiResponse(true, "USER FAVOURITES", new HashSet<>(favourites));
    }

    ProductDtoByIdForUser generateProductDto(Product product) {
        Discount discount = product.getDiscount();
        boolean valid = discount != null && DAYS.between(LocalDate.now(), discount.getEndDate()) >= 0;
        double value = product.getOutcomePrice().doubleValue();
        Set<ColorAttachmentDto> colorAttachmentDtos = productService.generateColorAttachments(product);

        List<SizeDto> sizeDtos = sizeRepository.findAllByActiveIsTrue().stream().map(this::generateSizeDto).collect(Collectors.toList());
        return new ProductDtoByIdForUser(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getBrand().getName(),
                product.getOutcomePrice(),
                valid ? discount.getPercent() : null,
                valid ? new BigDecimal(value - (value * discount.getPercent().doubleValue() / 100)) : product.getOutcomePrice(), new HashSet<>(sizeDtos), colorAttachmentDtos, true);
    }

    public SizeDto generateSizeDto(Size size){
        return new SizeDto(size.getId(),size.getName(),size.isActive());
    }
    public ApiResponse deleteAttachment(User user) {
        user.setAttachment(null);
        userRepo.save(user);
        return new ApiResponse(true, "O`chirildi");
    }
}
