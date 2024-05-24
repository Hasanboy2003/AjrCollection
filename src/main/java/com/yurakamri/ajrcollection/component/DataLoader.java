package com.yurakamri.ajrcollection.component;

import com.yurakamri.ajrcollection.entity.OrderStatus;
import com.yurakamri.ajrcollection.entity.Role;
import com.yurakamri.ajrcollection.entity.SmsForToken;
import com.yurakamri.ajrcollection.entity.User;
import com.yurakamri.ajrcollection.entity.enums.OrderStatusEnum;
import com.yurakamri.ajrcollection.entity.enums.RoleEnum;
import com.yurakamri.ajrcollection.repository.OrderStatusRepo;
import com.yurakamri.ajrcollection.repository.RoleRepo;
import com.yurakamri.ajrcollection.repository.SmsForApiRepo;
import com.yurakamri.ajrcollection.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final SmsForApiRepo smsForApiRepo;
    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final OrderStatusRepo orderStatusRepo;

    @Value("${spring.sql.init.mode}")
    private String mode;

    @Override
    public void run(String... args) {
        if(!orderStatusRepo.existsByName(OrderStatusEnum.NEW))
            orderStatusRepo.save(new OrderStatus(OrderStatusEnum.NEW,"for new orders"));
        if(!orderStatusRepo.existsByName(OrderStatusEnum.REJECTED))
            orderStatusRepo.save(new OrderStatus(OrderStatusEnum.REJECTED,"for rejected orders"));
        if(!orderStatusRepo.existsByName(OrderStatusEnum.DELIVERED))
            orderStatusRepo.save(new OrderStatus(OrderStatusEnum.DELIVERED,"for delivered orders"));
        if(!orderStatusRepo.existsByName(OrderStatusEnum.IN_PROGRESS))
            orderStatusRepo.save(new OrderStatus(OrderStatusEnum.IN_PROGRESS,"for in_progress orders"));
        if(!orderStatusRepo.existsByName(OrderStatusEnum.SOLD))
            orderStatusRepo.save(new OrderStatus(OrderStatusEnum.SOLD,"for sold orders"));
        if (mode.equals("always")) {
            smsForApiRepo.save(new SmsForToken());

            if (!roleRepo.existsByRoleName(RoleEnum.ROLE_SUPER_ADMIN)) {
                roleRepo.save(new Role(RoleEnum.ROLE_SUPER_ADMIN, "Admin tomonidan barcha ishlarni qila oladi"));
            }
            if (!roleRepo.existsByRoleName(RoleEnum.ROLE_USER)) {
                roleRepo.save(new Role(RoleEnum.ROLE_USER, "Oddiy User"));
            }
            if (!roleRepo.existsByRoleName(RoleEnum.ROLE_DELIVERER)) {
                roleRepo.save(new Role(RoleEnum.ROLE_DELIVERER, "Buyurtmalarni yetkazib beradi"));
            }

            String adminPhoneNumber = "+998917982322";
            User superAdmin = new User(adminPhoneNumber,
                    "Super Admin",
                    "Admin", "123456",
                    roleRepo.getByRoleName(RoleEnum.ROLE_SUPER_ADMIN));

            if (!userRepo.existsByPhoneNumber(adminPhoneNumber)) {
                superAdmin.setEnabled(true);
                userRepo.save(superAdmin);
            }
        }
    }
}
