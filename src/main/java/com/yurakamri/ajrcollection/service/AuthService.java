package com.yurakamri.ajrcollection.service;

import com.yurakamri.ajrcollection.entity.ChangePhoneNumber;
import com.yurakamri.ajrcollection.entity.User;
import com.yurakamri.ajrcollection.entity.enums.RoleEnum;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.ChangeProfile;
import com.yurakamri.ajrcollection.payload.req.auth.LoginDto;
import com.yurakamri.ajrcollection.payload.req.auth.RegisterDto;
import com.yurakamri.ajrcollection.payload.req.auth.VerificationDto;
import com.yurakamri.ajrcollection.repository.ChangePhoneNumberRepo;
import com.yurakamri.ajrcollection.repository.RoleRepo;
import com.yurakamri.ajrcollection.repository.UserRepo;
import com.yurakamri.ajrcollection.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.yurakamri.ajrcollection.payload.ApiResponse.response;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    // 30 - minutes
    private static final long TIME_FOR_USER_TO_BE_DELETED = 1_800_000L;

    // 2 - minutes
    private static final long TIME_FOR_VERIFICATION_CODE_TO_BE_SET_TO_NULL = 120_000L;

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @Autowired
    Timer timer;
    private final ModelMapper mapper;
    private final SmsService smsService;
    private final JwtProvider jwtProvider;
    private final ChangePhoneNumberRepo changePhoneNumberRepo;

    public ResponseEntity<ApiResponse> register(RegisterDto registerDto) {
        if (userRepo.existsByPhoneNumber(registerDto.getPhoneNumber())){
            return response(false, "USER WITH PHONE NUMBER : " + registerDto.getPhoneNumber() + " ALREADY EXISTS!");
        }

        User user = mapper.map(registerDto, User.class);
        user.setCode(generateVerificationCode());
        user.setRole(roleRepo.getByRoleName(RoleEnum.ROLE_USER));

        user = userRepo.save(user);

        schedule(taskDeleteUser(user.getId()), TIME_FOR_USER_TO_BE_DELETED);
        smsService.send_sms(user.getPhoneNumber(), user.getCode());

        return response("Verification code sent!",user.getCode());
    } // tested

    public ResponseEntity<ApiResponse> login(LoginDto dto) {
        Optional<User> optionalUser = userRepo.findByPhoneNumberAndEnabled(dto.getPhoneNumber(), true);
        return changeCodeAndSend(optionalUser);
    } // tested

    public ResponseEntity<ApiResponse> changePhoneNumber(ChangeProfile dto, User user) {
        Optional<User> optionalUser = userRepo.findById(user.getId());
        if (optionalUser.isPresent()) {
            boolean existsByPhoneNumber = userRepo.existsByPhoneNumber(dto.getPhoneNumber());
            if (existsByPhoneNumber) {
                if (!user.getPhoneNumber().equals(dto.getPhoneNumber()))
                    return response(false, "Bunday telifon raqamli foydalanuvchi mavjud");
            }

        }

        if (!dto.getFirstName().equals(user.getFirstName()) && dto.getFirstName() != null) {
            optionalUser.get().setFirstName(dto.getFirstName());
        }
        if (!dto.getLastName().equals(user.getLastName()) && dto.getFirstName() != null) {
            optionalUser.get().setLastName(dto.getLastName());
        }

        userRepo.save(optionalUser.get());
        if (!user.getPhoneNumber().equals(dto.getPhoneNumber())) {
            ChangePhoneNumber changePhoneNumber = changePhoneNumberRepo.save(new ChangePhoneNumber(
                            user,
                            dto.getPhoneNumber(),
                            generateVerificationCode(),
                            false,
                            true
                    )
            );

            smsService.send_sms(changePhoneNumber.getTempPhoneNumber(), changePhoneNumber.getCode());
            schedule(taskSetChangePhoneNumberCodeToNull(changePhoneNumber.getId()), TIME_FOR_VERIFICATION_CODE_TO_BE_SET_TO_NULL);
            return response( changePhoneNumber.getCode(),true);
        }
        return response("Profil tahrirlandi",false);
    }

    public ResponseEntity<ApiResponse> verifyAccount(VerificationDto dto) {
        Optional<User> optionalUser = userRepo.findByPhoneNumberAndCodeAndEnabled(dto.getPhoneNumber(), dto.getCode(), false);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(true);
            return setCodeToNullAndReturnJwt(user);
        } else {
            return response(false, "VERIFICATION ERROR!");
        }
    }

    public ResponseEntity<ApiResponse> verifyPhoneNumber(VerificationDto dto) {
        Optional<User> optionalUser = userRepo.findByPhoneNumberAndCodeAndEnabled(dto.getPhoneNumber(), dto.getCode(), true);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return setCodeToNullAndReturnJwt(user);
        }
        return response(false, "USER WITH PHONE NUMBER : " + dto.getPhoneNumber() + " OR CODE : " + dto.getCode() + " NOT FOUND!");
    }

    public ResponseEntity<ApiResponse> verifyPhoneNumberChange(VerificationDto dto, UserDetails userDetails) {
        User currentUser = verifyCurrentUser(userDetails);
        Optional<ChangePhoneNumber> optional = changePhoneNumberRepo
                .findByTempPhoneNumberAndCodeAndVerifiedFalseAndActiveTrueAndUser_Id(
                        dto.getPhoneNumber(),
                        dto.getCode(),
                        currentUser.getId()
                );

        if (optional.isPresent()) {
            ChangePhoneNumber changePhoneNumber = optional.get();
            changePhoneNumber.setActive(false);
            changePhoneNumber.setVerified(true);
            changePhoneNumber.setCode(null);
            changePhoneNumberRepo.save(changePhoneNumber);

            if (!userRepo.existsByPhoneNumberAndEnabled(changePhoneNumber.getTempPhoneNumber(), true)) {
                currentUser.setPhoneNumber(changePhoneNumber.getTempPhoneNumber());
                currentUser = userRepo.save(currentUser);
                return response(true, "Phone number changed to : " + currentUser.getPhoneNumber() + "!");
            }
            return response(false, "USER WITH PHONE NUMBER : " + changePhoneNumber.getTempPhoneNumber() + " ALREADY EXISTS!");
        }
        return response(false, "COULD NOT VERIFY PHONE NUMBER TO CHANGE!");
    }

    public ResponseEntity<ApiResponse> resendAccountVerificationCode(LoginDto dto) {
        Optional<User> optionalUser = userRepo.findByPhoneNumberAndEnabled(dto.getPhoneNumber(), false);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setCode(generateVerificationCode());
            user = userRepo.save(user);
            smsService.send_sms(user.getPhoneNumber(), user.getCode());
            return response(true, user.getCode());
        }
        return response(false, "VERIFICATION ERROR!");
    }

    public ResponseEntity<ApiResponse> resendPhoneNumberVerificationCode(LoginDto dto) {
        Optional<User> optionalUser = userRepo.findByPhoneNumberAndEnabled(dto.getPhoneNumber(), true);
        return changeCodeAndSend(optionalUser);
    }

    public ResponseEntity<ApiResponse> resendPhoneNumberChangeCode(LoginDto dto, UserDetails userDetails) {
        User currentUser = verifyCurrentUser(userDetails);
        Optional<ChangePhoneNumber> optional = changePhoneNumberRepo
                .findByTempPhoneNumberAndCodeIsNotNullAndVerifiedFalseAndActiveFalseAndUser_Id(
                        dto.getPhoneNumber(),
                        currentUser.getId()
                );
        if (optional.isPresent()) {
            ChangePhoneNumber changePhoneNumber = optional.get();
            changePhoneNumber.setActive(true);
            changePhoneNumber.setCode(generateVerificationCode());
            changePhoneNumber = changePhoneNumberRepo.save(changePhoneNumber);

            schedule(taskSetChangePhoneNumberCodeToNull(changePhoneNumber.getId()), TIME_FOR_VERIFICATION_CODE_TO_BE_SET_TO_NULL);
            smsService.send_sms(changePhoneNumber.getTempPhoneNumber(), changePhoneNumber.getCode());

            return response(true, changePhoneNumber.getCode());
        }
        return response(false, "RESEND ERROR!");
    }

    private ResponseEntity<ApiResponse> changeCodeAndSend(Optional<User> optionalUser) {
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // todo : search qilib bo'linganidan keyin olib tashlanadi
            if (user.getRole().getRoleName() != RoleEnum.ROLE_SUPER_ADMIN) {
                user.setCode(generateVerificationCode());
                user = userRepo.save(user);
            }

            smsService.send_sms(user.getPhoneNumber(), user.getCode());

            // todo : search qilib bo'linganidan keyin olib tashlanadi
            if (user.getRole().getRoleName() != RoleEnum.ROLE_SUPER_ADMIN) {
                schedule(taskSetUserCodeToNull(user.getId(), user.getPhoneNumber(), user.getCode()), TIME_FOR_VERIFICATION_CODE_TO_BE_SET_TO_NULL);
            }
            return response("Verification code sent!", user.getCode());
        }
        return response(false, "USER NOT FOUND!");
    }

    private ResponseEntity<ApiResponse> setCodeToNullAndReturnJwt(User user) {
        // todo : search qilib bolingandan keyin ochirib tashlanadi
        if (user.getRole().getRoleName() != RoleEnum.ROLE_SUPER_ADMIN) {
            user.setCode(null);
            user = userRepo.save(user);
        }
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getAuthorities()));
        return response(HttpStatus.OK, jwtProvider.generateToken(user));
    }

    private User verifyCurrentUser(UserDetails userDetails) {
        Objects.requireNonNull(userDetails);
        return userRepo
                .findByPhoneNumber(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("USER WITH PHONE NUMBER : " + userDetails.getUsername() + " NOT FOUND!"));
    }

    private void schedule(TimerTask task, Long after) {
        Date current = new Date();
        timer.schedule(task, new Date(current.getTime() + after));
    }

    private TimerTask taskDeleteUser(UUID userId) {
        return new TimerTask() {

            @Override
            public void run() {
                Optional<User> optionalUser = userRepo.findByIdAndEnabledFalse(userId);
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    if (!user.isEnabled() && !Objects.isNull(user.getCode())) {
                        userRepo.delete(user);
                    }
                }
            }
        };
    }

    private TimerTask taskSetUserCodeToNull(UUID userId, String phoneNumber, String currentCode) {
        return new TimerTask() {

            @Override
            public void run() {
                Optional<User> optionalUser = userRepo.findByIdAndPhoneNumberAndCodeAndEnabled(userId, phoneNumber, currentCode, true);
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    user.setCode(null);
                    userRepo.save(user);
                }
            }
        };
    } // done

    private TimerTask taskSetChangePhoneNumberCodeToNull(UUID changePhoneNumberId) {
        return new TimerTask() {

            @Override
            public void run() {
                Optional<ChangePhoneNumber> optional = changePhoneNumberRepo.findById(changePhoneNumberId);
                if (optional.isPresent()) {
                    ChangePhoneNumber changePhoneNumber = optional.get();
                    if (!changePhoneNumber.isVerified() && changePhoneNumber.getCode() != null && changePhoneNumber.isActive()) {
                        changePhoneNumber.setActive(false);
                        changePhoneNumberRepo.save(changePhoneNumber);
                    }
                }
            }
        };
    }

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo
                .findByPhoneNumber(username)
                .orElseThrow(() -> new UsernameNotFoundException("USER WITH PHONE NUMBER : " + username + " NOT FOUND!"));
    }

    public ResponseEntity<ApiResponse> loginDriver(LoginDto dto) {
        Optional<User> optionalUser = userRepo.findByPhoneNumberAndEnabled(dto.getPhoneNumber(), true);
        if(optionalUser.isEmpty())
            return response(false,"User doesn't exist!");

        User user = optionalUser.get();
        if(!user.getRole().getRoleName().name().equals(RoleEnum.ROLE_DELIVERER.name()))
            return response(false,"You can't login");
      return   changeCodeAndSend(optionalUser);
    }

}
