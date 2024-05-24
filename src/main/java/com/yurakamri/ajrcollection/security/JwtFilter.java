package com.yurakamri.ajrcollection.security;

import com.yurakamri.ajrcollection.controller.BaseUrl;
import com.yurakamri.ajrcollection.entity.User;
import com.yurakamri.ajrcollection.entity.enums.RoleEnum;
import com.yurakamri.ajrcollection.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserRepo userRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = extractJwtFromRequest(request);

        // VALIDATE TOKEN
        if (jwt == null || !jwtProvider.validateToken(jwt, request)) {
            filterChain.doFilter(request, response);
            return;
        }

        UUID userId = jwtProvider.getUserIdFromTokenClaims(jwt);
        Optional<User> optionalUser = userRepo.findById(userId);

        // CHECK USER
        if (optionalUser.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        User user = optionalUser.get();

        // IF USER TRIES TO ACCESS WEB URLS THEN DENY THE ACCESS
        if (request.getContextPath().startsWith(BaseUrl.BASE_URL_WEB) && user.getRole().getRoleName() != RoleEnum.ROLE_SUPER_ADMIN) {
            System.err.println("USER CANNOT ACCESS WEB URL");
            // todo : User Web ga so'rov yuborsa mana shu yerdan ham qaytarib yuborsa bo'ladi
        }

        // todo IMPLEMENT DEVICE EXISTING LOGIC

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }
}
